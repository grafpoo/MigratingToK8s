FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=profile-mvc-1.0.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]