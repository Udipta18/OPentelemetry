FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} abc.jar
ENTRYPOINT ["java","-jar","/abc.jar"]
EXPOSE 8080