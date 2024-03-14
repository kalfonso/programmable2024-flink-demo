# Flink Demos for Programmable 2024
## Riding The Streams: Stream Processing with Flink at CashApp

The demos include:
* Hypothetical fraud detection application demonstrating core elements of the Flink data streams programming model
* Flink SQL demo to show how it is possible to write a streaming application using only SQL.

# Requirements
* Docker: required to run Kafka via docker-compose
* Gradle 7.6 or later version
* Java 11 or later

# How to Run
From the root of the project run the commands below.

You can run load runner, Flink app and fraud payments consumer in different terminal or run them
in the background.

Modify the heuristic and load patterns to see
changes on how the streaming application detects fraudulent payments.

## Build
`./gradlew build`

## Start Kafka
`docker-compose up -d`

## Setup topics
`./scripts/setup_topics.sh`

## Run Flink app
`./scripts/run_fraud_detection.sh`

## Simulate payment traffic
`./scripts/run_load.sh`

## Show fraudulent payments
`./scripts/show_fraudulent_payments.sh`

## Stop Kafka
`docker-compose down`