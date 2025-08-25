FROM eclipse-temurin:17-jdk

# JAR 파일 복사
COPY /build/libs/FilmPass-0.0.1-SNAPSHOT.jar app.jar

# Elastic APM Agent 복사 (프로젝트 루트에 elastic-apm-agent.jar 파일이 있어야 함)
COPY elastic-apm-agent-1.55.0.jar /elastic-apm-agent.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java \
-javaagent:/elastic-apm-agent.jar \
-Delastic.apm.server_urls=https://filmpasses.apm.ap-northeast-2.aws.elastic-cloud.com \
-Delastic.apm.secret_token=0NdFKil2LasbMqpYoZ \
-Delastic.apm.service_name=FilmPass \
-Delastic.apm.environment=local \
-Delastic.apm.application_packages=com.example.filmpass \
-Delastic.apm.transaction_sample_rate=1.0 \
-Delastic.apm.capture_body=all \
-Delastic.apm.log_level=INFO \
-jar /app.jar"]