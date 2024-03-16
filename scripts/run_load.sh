#!/usr/bin/env bash

docker exec -it jobmanager java -cp /app/libs/flink-demo.jar com.demo.flink.load.LoadGeneratorKt