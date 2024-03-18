SET 'table.local-time-zone' = 'UTC';
SET 'pipeline.name' = 'flink-sql-fraud-detection';

CREATE TABLE PAYMENTS
(
    `id`          STRING,
    `sender_id`   STRING,
    `receiver_id` STRING,
    `amount`      BIGINT,
    `created_at`  BIGINT,
    `created_at_ts` AS TO_TIMESTAMP_LTZ(`created_at`, 3),
    WATERMARK FOR created_at_ts AS created_at_ts - INTERVAL '30' MINUTE
)
WITH (
      'connector' = 'kafka',
      'topic' = 'payment_events',
      'properties.bootstrap.servers' = 'broker:29092',
      'properties.group.id' = 'fraud-detection-sql',
      'scan.startup.mode' = 'earliest-offset',
      'format' = 'protobuf',
      'protobuf.message-class-name' = 'com.demo.flink.Payments$PaymentEvent'
      );

CREATE TABLE FRAUDULENT_PAYMENTS
(
    `customer_id` STRING,
    `total_amount` BIGINT,
    `total_count` BIGINT,
    `start_time` BIGINT,
    `end_time` BIGINT
)
WITH (
      'connector' = 'kafka',
      'topic' = 'fraudulent_payment_events_sql',
      'properties.bootstrap.servers' = 'broker:29092',
      'properties.group.id' = 'fraudulent-payments-sql',
      'format' = 'protobuf',
      'protobuf.message-class-name' = 'com.demo.flink.FraudulentPayments$FraudulentPaymentEvent'
      );

INSERT INTO FRAUDULENT_PAYMENTS
SELECT
    sender_id as customer_id,
    SUM(amount) as total_amount,
    COUNT(*) as `total_count`,
    1000 * UNIX_TIMESTAMP(CAST(window_start AS STRING)) + EXTRACT(MILLISECOND FROM window_start) as `start_time`,
    1000 * UNIX_TIMESTAMP(CAST(window_end AS STRING)) + EXTRACT(MILLISECOND FROM window_end) as `end_time`
FROM TABLE(
    TUMBLE(TABLE PAYMENTS, DESCRIPTOR(created_at_ts), INTERVAL '30' MINUTES))
GROUP BY sender_id, window_start, window_end
HAVING SUM(amount) > 3000 or COUNT(*) > 3;
