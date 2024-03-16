package com.demo.flink

import com.demo.flink.FraudulentPayments.FraudulentPaymentEvent
import com.demo.flink.Payments.PaymentEvent
import com.demo.flink.model.CustomerPayment
import com.demo.flink.model.CustomerPayments
import com.demo.flink.model.FraudulentPaymentConverterFunction
import com.demo.flink.model.FraudulentPaymentsFunction
import com.demo.flink.model.ToCustomerPaymentMapFunction
import com.demo.flink.serdes.FraudulentPaymentEventSerializationSchema
import com.demo.flink.serdes.PaymentEventDeserializationSchema
import com.twitter.chill.protobuf.ProtobufSerializer
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.connector.kafka.sink.KafkaSink
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import java.time.Duration

fun main() {
  val bootstrapServers = "broker:29092"

  val source = KafkaSource.builder<PaymentEvent>()
    .setBootstrapServers(bootstrapServers)
    .setTopics("payment_events")
    .setStartingOffsets(OffsetsInitializer.earliest())
    .setDeserializer(PaymentEventDeserializationSchema())
    .build()

  val sinkTopic = "fraudulent_payment_events"
  val sink = KafkaSink.builder<FraudulentPaymentEvent>()
    .setBootstrapServers(bootstrapServers)
    .setRecordSerializer(FraudulentPaymentEventSerializationSchema(sinkTopic))
    .build()

  val app = FraudDetectionApp(source, sink)
  app.execute()
}

class FraudDetectionApp(
  private val source: KafkaSource<PaymentEvent>,
  private val sink: KafkaSink<FraudulentPaymentEvent>
) {
  fun execute() {
    val env = StreamExecutionEnvironment.getExecutionEnvironment()
    env.config.registerTypeWithKryoSerializer(
      PaymentEvent::class.java,
      ProtobufSerializer::class.java
    )
    env.config.registerTypeWithKryoSerializer(
      FraudulentPaymentEvent::class.java,
      ProtobufSerializer::class.java
    )

    val watermarkStrategy = WatermarkStrategy
      .forBoundedOutOfOrderness<PaymentEvent>(Duration.ofMinutes(30))
      .withTimestampAssigner { e, _ -> e.createdAt }

    env.fromSource(source, watermarkStrategy, "payments_source")
      .uid("payment_events_source")
      .map(ToCustomerPaymentMapFunction())
      // Must use anonymous object with generic super type as recommended in this issue:
      // https://youtrack.jetbrains.com/issue/KT-48422/Interface-type-could-not-be-inferred-in-Kotlin-1.5.10-Apache-Flink-runtime-exceptions
      .keyBy(object : KeySelector<CustomerPayment, String> {
        override fun getKey(payment: CustomerPayment): String {
          return payment.senderID
        }
      })
      .window(TumblingEventTimeWindows.of(Time.minutes(30)))
      .allowedLateness(Time.hours(1))
      .aggregate(FraudulentPaymentsFunction(maxCount = 3, maxAmount = 3000))
      .uid("fraudulent_payments")
      .filter(CustomerPayments::fraudulent)
      .map(FraudulentPaymentConverterFunction())
      .uid("fraudulent_payments_events")
      .sinkTo(sink)
      .uid("fraudulent_events_sink")

    env.execute("Fraud Detection App")
  }
}
