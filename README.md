# phone-sector project

## Overview

This service was built with the goal of implementing a single endpoint (`/agggregate`) that is responsible to receive
a POST request with a list of number to aggregate, based on the sector and prefix. In the topics below, we describe
the process, from the moment a request is made:

1. For each number on the list:
    1. Check number is validated (through a regex expression)
    2. If valid we try to get the prefix, otherwise go back to (1)
    3. If prefix exists we check if the number is in redis cache, otherwise go back to (1)
    4. If number is in redis cache we go back to (1), otherwise we request the number sector to the phone sector api
2. We aggregate the response based on the prefix with the sectors and number of phones per a sector.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `phone-sector-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.

## Launch docker setup

The docker environment needs the following steps, to be created:

- Build the package:
```shell script
mvn package
```

- Launch the containers:
```shell script
docker-compose up web
```
