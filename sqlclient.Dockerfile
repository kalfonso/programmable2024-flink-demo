FROM flink:1.17.2

RUN wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-protobuf/1.17.2/flink-protobuf-1.17.2.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-kafka/1.17.2/flink-sql-connector-kafka-1.17.2.jar;

COPY flink-sql-demo/build/libs/flink-sql-demo.jar /opt/flink/lib
COPY flink-sql-demo/conf/flink-conf.yaml /opt/flink/conf/