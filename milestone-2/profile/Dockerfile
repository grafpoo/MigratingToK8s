FROM ubuntu:18.04
RUN apt-get update && \
    apt-get install -y telnet && \
    apt-get install -y net-tools && \
    apt-get install -y software-properties-common && \
    add-apt-repository ppa:openjdk-r/ppa && \
    apt-get update && \
	apt-get install -y openjdk-11-jdk
ARG JAR_FILE=profile-mvc-1.0.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]