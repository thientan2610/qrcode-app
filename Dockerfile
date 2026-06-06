Dockerfile
# Bước 1: Dùng môi trường Maven để biên dịch code Java
FROM maven:3.8.8-openjdk-17 AS build
COPY . .
RUN ./mvnw clean package -DskipTests

# Bước 2: Dùng môi trường Java 17 siêu nhẹ để chạy ứng dụng
FROM openjdk:17-jdk-slim
COPY --from=build target/qrcode_app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]