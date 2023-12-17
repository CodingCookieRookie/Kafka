#!/bin/bash
brew install kafka
lsof -i :2181 -sTCP:LISTEN |awk 'NR > 1 {print $2}'  |xargs kill -9 #kill all processes with port 2181, do with caution
lsof -i :50112 -sTCP:LISTEN |awk 'NR > 1 {print $2}'  |xargs kill -9
rm -rf /opt/homebrew/var/lib/kafka-logs #delete all kafka logs, do with caution
zookeeper-server-start /opt/homebrew/etc/kafka/zookeeper.properties &
sleep 5
kafka-server-start /opt/homebrew/etc/kafka/server.properties & #change /opt/homebrew/ to /usr/local/ for intel
echo "kafka started, you can run java main now"