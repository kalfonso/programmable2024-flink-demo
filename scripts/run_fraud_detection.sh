#!/usr/bin/env bash

java -cp flink-demo/build/libs/flink-demo.jar \
     --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-opens=java.base/java.util=ALL-UNNAMED \
     com.demo.flink.FraudDetectionAppKt