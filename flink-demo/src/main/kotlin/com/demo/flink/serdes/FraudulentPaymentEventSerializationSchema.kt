package com.demo.flink.serdes

import com.demo.flink.FraudulentPayments
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema
import org.apache.kafka.clients.producer.ProducerRecord

class FraudulentPaymentEventSerializationSchema(
  private val topic: String
) : KafkaRecordSerializationSchema<FraudulentPayments.FraudulentPaymentEvent> {
  override fun serialize(
    event: FraudulentPayments.FraudulentPaymentEvent,
    context: KafkaRecordSerializationSchema.KafkaSinkContext,
    timestamp: Long
  ): ProducerRecord<ByteArray, ByteArray> {
    return ProducerRecord(
      topic,
      event.customerId.toByteArray(Charsets.UTF_8),
      event.toByteArray()
    )
  }
}
