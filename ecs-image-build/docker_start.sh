#!/bin/bash
#
# Start script for item-group-consumer
#
PORT=8080

exec java -jar -Dserver.port="${PORT}" "item-group-consumer.jar"
