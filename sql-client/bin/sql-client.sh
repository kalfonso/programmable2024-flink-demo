#!/bin/bash

set -o xtrace

${FLINK_HOME}/bin/sql-client.sh -l ${SQL_CLIENT_HOME}/lib $@