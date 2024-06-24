FROM maven:3.8-openjdk-17-slim as builder

WORKDIR /tp-tacs
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-alpine

COPY --from=builder /tp-tacs /app
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "target/tacs-grupo1-1.0-SNAPSHOT.jar"]
