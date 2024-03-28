FROM flink:1.17.2

# Copy sql-client custom script
COPY sql-client/bin/* /opt/sql-client/
# Create CLI lib folder
RUN mkdir -p /opt/sql-client/lib

RUN wget -P /opt/sql-client/lib https://repo.maven.apache.org/maven2/org/apache/flink/flink-protobuf/1.17.2/flink-protobuf-1.17.2.jar; \
    wget -P /opt/sql-client/lib https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-kafka/1.17.2/flink-sql-connector-kafka-1.17.2.jar; \
    wget -P /opt/sql-client/lib https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-kafka/1.17.2/flink-connector-kafka-1.17.2.jar; \
    wget -P /opt/sql-client/lib https://repo1.maven.org/maven2/org/apache/kafka/kafka-clients/3.2.3/kafka-clients-3.2.3.jar;

# Copy configuration
COPY sql-client/conf/* /opt/flink/conf/

WORKDIR /opt/sql-client
ENV SQL_CLIENT_HOME /opt/sql-client

COPY flink-sql-demo/build/libs/flink-sql-demo.jar /opt/flink/lib