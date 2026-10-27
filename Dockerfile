# 1단계: 빌드 스테이지
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# 빌드 캐시 활용을 위해 설정 파일들을 먼저 복사
COPY build.gradle settings.gradle /app/
COPY src /app/src

# 실제 빌드 진행 (테스트는 건너뛰어 빌드 속도 향상)
RUN gradle clean bootJar -x test

# 2단계: 실행 스테이지
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 1단계 빌드 스테이지에서 생성된 jar 파일만 추출하여 복사
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

# Render 등 환경에서 포트 바인딩을 위한 설정 (Spring Boot 기본 포트 8080)
EXPOSE 8080

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "-Dserver.port=8080", "app.jar"]