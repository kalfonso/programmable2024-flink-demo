#!/usr/bin/env bash

docker exec -it jobmanager flink run /app/libs/flink-demo.jar
#java -cp flink-demo/build/libs/flink-demo.jar \
#     --add-opens java.base/java.lang=ALL-UNNAMED \
#     --add-opens=java.base/java.util=ALL-UNNAMED \
#     com.demo.flink.FraudDetectionAppKt