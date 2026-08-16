# Stage 1: Build stage with Maven & OpenJDK 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml and pre-fetch dependencies (caching layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy application source code and package executable JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage with slim Eclipse Temurin JRE 21 Alpine
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root system user for security hardening
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy built artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set ownership
RUN chown -R appuser:appgroup /app
USER appuser

# Expose default port (Render overrides with PORT env var)
EXPOSE 8080

# Launch executable JAR with UTC timezone, optimized G1GC and container memory limits
ENTRYPOINT ["java", "-Duser.timezone=UTC", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
