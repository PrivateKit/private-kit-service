FROM maven:3.6.3-openjdk-11 AS build
RUN mkdir -p /app/
RUN mkdir -p /app/src/
COPY src /app/src
COPY pom.xml /app
RUN mvn -B -f /app/pom.xml clean package -DskipTests

FROM openjdk:11-jdk-stretch
VOLUME /tmp
EXPOSE 8080
RUN mkdir -p /app/logs/
COPY --from=build /app/target/private-kit-service-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/app/app.jar"]
