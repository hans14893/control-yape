# Multi-stage build para producción
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Runtime image - solo lo necesario
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /build/target/control-yape-1.0.0-SNAPSHOT.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD java -cp app.jar org.springframework.boot.loader.Main --check || exit 1

# Exponer puerto
EXPOSE 9090

# Variables de entorno requeridas
ENV SPRING_PROFILES_ACTIVE=prod

# Ejecutar con usuario no-root
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser
USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
