#!/usr/bin/env bash

# when joining a network
docker run -it --net=springsstable_infrastructure --link cassandra_dev --rm cassandra:3.9 cqlsh cassandra_dev

CREATE KEYSPACE IF NOT EXISTS spring_sstable
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE spring_sstable;

CREATE TABLE historical_prices (
    ticker ascii,
    date timestamp,
    open decimal,
    high decimal,
    low decimal,
    close decimal,
    volume bigint,
    adj_close decimal,
    PRIMARY KEY (ticker, date)
) WITH CLUSTERING ORDER BY (date DESC);



CREATE KEYSPACE IF NOT EXISTS quote
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE quote;

CREATE TABLE historical_prices (
    ticker ascii,
    date timestamp,
    open decimal,
    high decimal,
    low decimal,
    close decimal,
    volume bigint,
    adj_close decimal,
    PRIMARY KEY (ticker, date)
) WITH CLUSTERING ORDER BY (date DESC);


== How to load into sstable using docker
sstableloader -d `host` `keyspace/table`
sstableloader -d cassandra_dev quote/historical_prices

CREATE KEYSPACE IF NOT EXISTS user_contacts
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

use user_contacts;

CREATE TYPE IF NOT EXISTS user_contacts.CONTACT_NAME (
    lastName TEXT,
    firstName TEXT,
    middleName TEXT
);


CREATE TABLE IF NOT EXISTS user_contacts.person (
    id UUID,
    name CONTACT_NAME,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY(id, created_at)
)WITH CLUSTERING ORDER BY (created_at ASC);



docker run -it --net=springsstable_infrastructure --link cassandra_dev --rm cassandra:3.9 bash

cd /sstables