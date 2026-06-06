# Bước 1: Biên dịch ứng dụng Java bằng bản Maven dùng Eclipse Temurin Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Bước 2: Chạy ứng dụng bằng gói Eclipse Temurin Java 21 siêu ổn định
FROM eclipse-temurin:21-jdk-alpine
COPY --from=build target/qrcode_app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
