#!/bin/sh
curl --location 'http://localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data '{
    "name": "property-connector",
    "config": {
        "connector.class": "io.debezium.connector.mysql.MySqlConnector",
        "database.allowPublicKeyRetrieval":"true",
        "database.hostname": "host.docker.internal",
        "database.port": "3306",
        "database.user": "root",
        "database.password": "root",
        "database.dbname": "resource",
        "table.include.list": "resource.spu",
        "topic.prefix": "cdc",
        "database.server.id": 1,
        "schema.history.internal.kafka.bootstrap.servers":  "kafka:9092",
        "schema.history.internal.kafka.topic": "schema-changes.db"
    }
}'