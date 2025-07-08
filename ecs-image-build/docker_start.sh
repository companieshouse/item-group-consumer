#!/bin/bash
#
# Start script for item-group-consumer
#
PORT=8080

exec java -jar -Dserver.port="${PORT}" -XX:MaxRAMPercentage=80 "item-group-consumer.jar"
