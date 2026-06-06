# Bước 1: Biên dịch ứng dụng Java bằng Maven
FROM maven:3.8.8-openjdk-17 AS build
COPY . .
RUN ./mvnw clean package -DskipTests

# Bước 2: Chạy ứng dụng bằng gói Java 17 Eclipse Temurin siêu ổn định
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build target/qrcode_app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
