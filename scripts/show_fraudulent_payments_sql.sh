#!/usr/bin/env sh

docker exec -it jobmanager java -cp /app/libs/flink-demo.jar com.demo.flink.fraudalerts.FraudAlertsConsumerKt fraudulent_payment_events_sql

