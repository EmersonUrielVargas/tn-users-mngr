FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
RUN apt-get update && apt-get install -y curl

RUN addgroup --system appgroup && adduser --system appuser && usermod -a -G appgroup appuser && chown -R appuser:appgroup /tmp
COPY *.jar tn-users-mngr.jar
ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar tn-users-mngr.jar" ]
