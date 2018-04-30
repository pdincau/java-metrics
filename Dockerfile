FROM openjdk:8-jre-alpine

WORKDIR /app

COPY target/java-metrics-jar-with-dependencies.jar ./app.jar

CMD ["java", "-jar", "-Dcom.sun.management.jmxremote", "-Djava.rmi.server.hostname=0.0.0.0", "-Dcom.sun.management.jmxremote.local.only=false", "-Dcom.sun.management.jmxremote.rmi.port=3333", "-Dcom.sun.management.jmxremote.port=3333", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.authenticate=false", "app.jar"]
