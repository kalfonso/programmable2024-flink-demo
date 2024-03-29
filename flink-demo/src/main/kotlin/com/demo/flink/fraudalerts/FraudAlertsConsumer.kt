package com.demo.flink.fraudalerts

import org.apache.kafka.clients.consumer.CommitFailedException
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

// FraudAlersConsumer consumes fraudulent payment events.
class FraudAlertsConsumer(
  private val topic: String,
  props: Properties
) {
  private val running = AtomicBoolean(true)

  private val pollTimeoutMillis = Duration.ofMillis(30000)

  private val consumer = KafkaConsumer(
    props,
    StringDeserializer(),
    FraudulentPaymentEventDeserializer()
  )

  fun stop() {
    running.set(false)
  }

  fun run() {
    consumer.subscribe(listOf(topic))

    while (running.get()) {
      val records = consumer.poll(pollTimeoutMillis)
      records.forEach {
        val payment = it.value()
        val from = payment.startTime
        val to = payment.endTime
        logger.info(
          "CustomerID: ${payment.customerId}, Amount: ${payment.totalAmount}, From: $from, " +
            "To: $to, Location: ${payment.location}"
        )
      }
      try {
        consumer.commitSync()
      } catch (e: CommitFailedException) {
        logger.error("committing offsets failed", e)
      }
    }
  }
}

private val logger = LoggerFactory.getLogger(FraudAlertsConsumer::class.java)

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    throw RuntimeException("no topic provided")
  }
  val topic = args[0]
  val properties = Properties()
  properties["bootstrap.servers"] = "broker:29092"
  // Generate a unique consumer group so the consumer always starts from the earliest offset
  properties["group.id"] = UUID.randomUUID().toString()
  properties["auto.offset.reset"] = "earliest"
  val consumer = FraudAlertsConsumer(topic, properties)

  // Stop consumer when JVM shutdowns
  Runtime.getRuntime().addShutdownHook(
    Thread {
      logger.info("Stopping consumer")
      consumer.stop()
    }
  )

  consumer.run()
}
