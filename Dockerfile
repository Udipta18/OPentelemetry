FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar abc.jar
ENTRYPOINT ["java","-jar","/abc.jar"]
EXPOSE 8080