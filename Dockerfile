# 빌드 스테이지(eclipse-temurin을 사용하여 jar 파일 생성)
FROM eclipse-temurin:21-jdk-jammy AS build

# 컨테이너 내부의 작업 디렉토리를 /app으로 설정
WORKDIR /workspace

# 의존성 캐시 단계
COPY gradlew .
COPY gradle gradle
COPY settings.gradle build.gradle ./

# Gradle 래퍼(/gradlew)에 실행 권한 부여 및 라이브러리 다운로드
RUN chmod +x ./gradlew && ./gradlew --no-daemon dependencies

# 빌드 단계
COPY src src
RUN ./gradlew --no-daemon clean bootJar -x test

# 실행 단계
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# 보안을 위한 비관리자 유저 생성
RUN useradd -ms /bin/bash appuser
USER appuser

# 빌드 결과물만 복사
COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080

# 한국 시간 설정 및 실행
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
