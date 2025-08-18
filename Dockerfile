FROM eclipse-temurin:17-jdk
COPY /build/libs/FilmPass-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]