FROM maven:3.8-openjdk-17-slim as builder

WORKDIR /diagnostic
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-alpine

COPY --from=builder /tp-tacs /app
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "target/diagnostic-1.0-SNAPSHOT.jar"]
