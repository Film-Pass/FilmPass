FROM eclipse-temurin:17-jdk
ENV JAVA_OPTS="-Xms512m -Xmx1g"
COPY /build/libs/FilmPass-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]