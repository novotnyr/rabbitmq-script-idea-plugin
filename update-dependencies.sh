#!/usr/bin/env bash
RABBITMQADMIN=~/.m2/repository/com/github/novotnyr/rabbitmqadmin/0.1-SNAPSHOT/rabbitmqadmin-0.1-SNAPSHOT-jar-with-dependencies.jar
if [ -f "$RABBITMQADMIN" ]
then
	cp "$RABBITMQADMIN" ./lib
	echo "Dependency copied to ./lib"
else
	echo "RabbitMQ Admin JAR is not available in local Maven repo" >&2
	exit 1;
fi;

