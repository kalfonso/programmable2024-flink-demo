#!/usr/bin/env sh

# Create source topic
docker exec -it broker kafka-topics --create --topic payment_events --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

# Create target topic data stream
docker exec -it broker kafka-topics --create --topic fraudulent_payment_events --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

# Create target topic flink sql
docker exec -it broker kafka-topics --create --topic fraudulent_payment_events_sql --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092