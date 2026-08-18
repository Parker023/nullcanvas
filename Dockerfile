# ---- Build stage: compile the executable (boot) jar ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew clean bootJar -x test --no-daemon

# ---- Run stage: minimal JRE image ----
FROM eclipse-temurin:21-jre
WORKDIR /app
# bootJar produces a single executable jar (no -plain jar), so this glob is unambiguous.
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 2399

# Tuned for a single small (512 MB) container: SerialGC beats G1 at this size,
# cap heap at 70% of the container, and shrink thread stacks.
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70 -XX:+UseSerialGC -Xss512k"

ENTRYPOINT ["java", "-jar", "app.jar"]
