#!/bin/sh

curl -s isaacgetto.com > /dev/null 2>&1
healthcheck=$?
echo "healthcheck: ${healthcheck}"

ps auxw | grep gradle | grep -v grep > /dev/null 2>&1 
running=$?
echo "running: ${running}"

if [ $healthcheck != 0 ]; then
	if [ $running = 0 ]; then
		echo "stopping..."
		pgrep -f java | xargs kill
	fi
	cd /root/moss
	export MOSS_HTML_DIR="/home/site"
	echo "starting..."
	./gradlew run > /dev/null 2>&1 &
	cd - > /dev/null 2>&1
fi
