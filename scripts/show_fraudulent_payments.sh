#!/usr/bin/env sh

docker exec -it jobmanager java -cp /app/libs/flink-demo.jar com.demo.flink.fraudalerts.FraudAlertsConsumerKt

#docker exec -it broker kafka-console-consumer \
#    --bootstrap-server localhost:9092 \
#    --topic $1 \
#    --from-beginning
