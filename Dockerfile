FROM ubuntu:16.04

MAINTAINER Pryahin Vladimir

RUN apt-get update
RUN apt-get install -y openjdk-8-jdk-headless
RUN apt-get install -y maven

ENV WORK /opt

COPY . $WORK/java/
COPY ./httpd.conf /etc
COPY ./httptest /var/www/html/httptest

WORKDIR $WORK/java
RUN mvn package

EXPOSE 80

CMD java -jar $WORK/java/target/highload-1.0-SNAPSHOT.jar
