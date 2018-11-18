FROM maven:3.6.0-jdk-8-slim

MAINTAINER David Pérez <dperezarbues@gmail.com>

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/
RUN mkdir -p /usr/src/subscriptionService/

RUN mvn package -DskipTests
RUN cp /tmp/target/subscriptionService*.jar /usr/src/subscriptionService/subscriptionService.jar
WORKDIR /usr/src/subscriptionService
EXPOSE 8443
CMD ["java", "-jar", "subscriptionService.jar"]
