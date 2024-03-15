package com.demo.flink.serdes

import com.demo.flink.Payments
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerRecord

// DeserializationSchema for Payments.PaymentEvent protobuf.
// The first operator in the Flink pipeline after the source must be able to deserialize this event.
class PaymentEventDeserializationSchema :
  KafkaRecordDeserializationSchema<Payments.PaymentEvent> {

  override fun getProducedType(): TypeInformation<Payments.PaymentEvent> {
    return TypeInformation.of(Payments.PaymentEvent::class.java)
  }

  override fun deserialize(
    record: ConsumerRecord<ByteArray, ByteArray>,
    out: Collector<Payments.PaymentEvent>
  ) {
    out.collect(Payments.PaymentEvent.parseFrom(record.value()))
  }
}
