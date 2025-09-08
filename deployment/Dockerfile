# syntax=docker/dockerfile:1.6
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# --- NUEVO ---
ARG GRADLE_CACHE_ID=default
ENV GRADLE_USER_HOME=/root/.gradle
# --------------

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew

# Descarga deps con cache de Gradle *aislado por servicio*
RUN --mount=type=cache,target=${GRADLE_USER_HOME},id=gradle-${GRADLE_CACHE_ID},sharing=locked \
    ./gradlew --no-daemon --stacktrace --info dependencies

# Código fuente
COPY . .

# Build con el mismo cache aislado
RUN --mount=type=cache,target=${GRADLE_USER_HOME},id=gradle-${GRADLE_CACHE_ID},sharing=locked \
    ./gradlew --no-daemon --stacktrace --info clean bootJar

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
RUN useradd -ms /bin/bash appuser
USER appuser
EXPOSE 8080
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]
