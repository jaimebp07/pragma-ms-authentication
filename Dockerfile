# Etapa de construcción
FROM gradle:8.14.3-jdk17 AS build
WORKDIR /home/gradle/project
COPY . .
#RUN gradle :app-service:build -x test
RUN gradle build -x test

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine
# instalar netcat para verificar disponibilidad de la DB
RUN apk add --no-cache netcat-openbsd

WORKDIR /app
COPY --from=build /home/gradle/project/applications/app-service/build/libs/msAuthentication.jar app.jar

# wait-for-postgres.sh debe estar guardado en LF
COPY wait-for-postgres.sh /wait-for-postgres.sh 
RUN chmod +x /wait-for-postgres.sh

EXPOSE 8080
ENTRYPOINT ["/wait-for-postgres.sh"]