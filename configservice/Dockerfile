FROM maven:3.6.0-jdk-11-slim

MAINTAINER David Pérez <dperezarbues@gmail.com>

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/
RUN mkdir -p /usr/src/configService/

RUN mvn package -DskipTests
RUN cp /tmp/target/configService*.jar /usr/src/configService/configService.jar
WORKDIR /usr/src/configService
EXPOSE 8001
CMD ["java", "-jar", "configService.jar"]
