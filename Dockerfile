FROM eclipse-temurin:17-jre-jammy

# create non-root user and group
# -l and static IDs assigned to avoid delay in lookups and system logging
ARG THE_USER_ID=1001
ARG THE_GROUP_ID=1001
RUN DEBIAN_FRONTEND=noninteractive && \
    /usr/sbin/groupadd -g $THE_GROUP_ID pixolution && \
    /usr/sbin/useradd -l -u $THE_USER_ID -G pixolution -g $THE_GROUP_ID pixolution && \
    mkdir logs && chgrp pixolution logs && chmod ug+rwx logs

# run as non-root
USER pixolution:pixolution

# main REST service and OpenAPI /swagger-ui/index.html
EXPOSE 8080

# Avoid warning log when starting the server (see https://github.com/google/guice/issues/1133)
#ENV JAVA_OPTS="-Xms512m -Xmx10g -server --add-opens java.base/java.lang=ALL-UNNAMED"

# Copy the sprint boot jar
COPY embeddings-grid.jar app.jar

CMD ["java","-jar","app.jar"]
