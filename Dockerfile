FROM eclipse-temurin:8-jammy  AS build

ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
COPY . $HOME

# https://dev.doroshev.com/blog/docker-mount-type-cache/
RUN ls -la
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f $HOME/pom.xml clean install

FROM eclipse-temurin:8-jammy
ARG JAR_FILE=/usr/app/target/*.jar
COPY --from=build $JAR_FILE /app/runner.jar
ENV DB_DIR=/var/lib/sqlite
RUN mkdir -p $DB_DIR && touch $DB_DIR/data.db
VOLUME $DB_DIR
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/runner.jar"]