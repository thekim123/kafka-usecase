# OpenJDK 17 이미지 사용
FROM openjdk:17-jdk-slim

ARG CACHE_BUST=2
RUN echo "Cache busting: $CACHE_BUST"

# JAR 파일 복사
COPY build/libs/jwt-redis-0.0.1-SNAPSHOT.jar app.jar

# 포트 노출 (예: 8080)
EXPOSE 9080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
