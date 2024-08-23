#deploy del backend con image 
FROM openjdk:17

WORKDIR /app

ADD ./target/diagnostic-1.0-SNAPSHOT.jar target/diagnostic-1.0-SNAPSHOT.jar

ENV MONGO_URL ${MONGO_STRING_CONECTION}
ENV MONGO_DB ${MONGO_DB}
ENV JWT_KEY ${JWT_KEY}

EXPOSE 8080

ENTRYPOINT ["java", "-jar","/app/target/diagnostic-1.0-SNAPSHOT.jar"]

