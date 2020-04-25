FROM ubuntu:18.04
RUN apt-get update && \
    apt-get install -y mysql-client && \
    apt-get install -y telnet && \
    apt-get install -y net-tools && \
    apt-get install -y software-properties-common && \
    add-apt-repository ppa:openjdk-r/ppa && \
    apt-get update && \
	apt-get install -y openjdk-11-jdk && \
	touch /tmp/sleep
ARG JAR_FILE=sleep.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]