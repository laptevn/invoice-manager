FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
RUN adduser -D -u 1000 testuser
USER testuser
WORKDIR /home/testuser
ADD invoice-manager-1.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]