FROM maven:3.6.0-jdk-8-slim

MAINTAINER David Pérez <dperezarbues@gmail.com>

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/
RUN mkdir -p /usr/src/eurekaServer/

RUN mvn package -DskipTests
RUN cp /tmp/target/eurekaServer*.jar /usr/src/eurekaServer/eurekaServer.jar
WORKDIR /usr/src/eurekaServer
EXPOSE 8010
CMD ["java", "-jar", "eurekaServer.jar"]
