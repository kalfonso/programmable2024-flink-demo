#!/bin/bash

echo Executing sql-client with extra arguments "$@"
${FLINK_HOME}/bin/sql-client.sh embedded -d ${FLINK_HOME}/conf/sql-client-conf.yaml -l ${SQL_CLIENT_HOME}/lib $@