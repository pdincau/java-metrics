# Java Metrics

Trivial java application to collect metrics using Dropwizard Metrics.

## How to build

```sh
mvn clean package
```

## How to run

```sh
java -jar target/java-metrics-jar-with-dependencies.jar
```

Default port is 8080.

## How to create docker image

```sh
docker build -t pdincau/java-metrics .
```

## How to run docker image

```sh
docker run --rm=true -it -p 8080:8080 -p 3333:3333 --name java-metrics pdincau/java-metrics
```

## JMX

You can connect to JMX at 127.0.0.1:3333 with Jconsole for example.

## Greeting route

You can check the health of the service by calling:

```sh
curl localhost:${PORT}/hello?yourname
```

## Healthcheck route

You can check the health of the service by calling:

```sh
curl localhost:${PORT}/ping
```
