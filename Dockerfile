FROM eclipse-temurin:17-jre-alpine
COPY build/libs/FilmPass-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]