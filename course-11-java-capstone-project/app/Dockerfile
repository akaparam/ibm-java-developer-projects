# -------- BUILD STAGE --------
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

RUN apt-get update && apt-get install -y \
    ca-certificates \
    curl \
    && rm -rf /var/lib/apt/lists/*

COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

COPY pom.xml .
ENV MAVEN_OPTS="-Dhttps.protocols=TLSv1.2 -Dhttp.keepAlive=false"
RUN ./mvnw -B -e -U -Dmaven.artifact.threads=1 dependency:go-offline

COPY src src
RUN ./mvnw -B -e -U -Dmaven.artifact.threads=1 package -DskipTests

# -------- RUNTIME STAGE --------
FROM eclipse-temurin:25-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
