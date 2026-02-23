# ---------- BUILD STAGE ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# copy pom first (for dependency caching)
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# copy source
COPY src ./src

# build jar
RUN mvn clean package -DskipTests


# ---------- RUN STAGE ----------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render provides PORT dynamically
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]