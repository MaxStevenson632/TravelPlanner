# ---------- Build stage ----------
FROM eclipse-temurin:25-jdk AS build

# Work inside /app
WORKDIR /app

# Copy the entire project and put it into docker image
COPY . .

# Make Gradle wrapper executable, so docker has permission to run
RUN chmod +x ./gradlew

# Build Spring Boot application, packages entire app into a .jar
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy the generated Spring Boot JAR
COPY --from=build /app/build/libs/*.jar app.jar

# Container will receive network traffic on port 8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]