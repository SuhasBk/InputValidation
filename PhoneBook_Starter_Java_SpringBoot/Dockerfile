FROM maven:3-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install

FROM scratch AS tests
COPY --from=build /home/app/target/site/ /tests/

FROM openjdk:17-ea-4-jdk-slim
COPY --from=build /home/app/target/assignment-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]