FROM maven:3.6.0-jdk-8-slim

MAINTAINER David Pérez <dperezarbues@gmail.com>

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/
RUN mkdir -p /usr/src/campaignClient/

RUN mvn package -DskipTests
RUN cp /tmp/target/campaignClient*.jar /usr/src/campaignClient/campaignClient.jar
WORKDIR /usr/src/campaignClient
EXPOSE 8442
CMD ["java", "-jar", "campaignClient.jar"]
