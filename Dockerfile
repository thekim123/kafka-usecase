# 1. 빌드 스테이지
FROM openjdk:17-jdk-slim AS build
WORKDIR /app

# 2. Gradle 설정 및 Wrapper 복사
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# 3. 의존성 캐싱
RUN ./gradlew dependencies --no-daemon

# 4. 소스 코드 복사 (최대한 나중에!)
COPY src /app/src

# 5. 빌드
RUN ./gradlew bootjar --no-daemon

# 6. 실행 스테이지
FROM openjdk:17-jdk-slim
WORKDIR /app

# 7. 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 8. 애플리케이션 실행
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
