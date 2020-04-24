FROM openjdk:8-alpine
# required app version for build to passed as default
# env value available during run phase, directly reading host
# env variables in dockerfile is not possible
ARG app_version
RUN echo the app_version: ${app_version}
ENV APP_VERSION=${app_version}
# move the compiled jar to container
COPY target/kgai-java-raw-${APP_VERSION}.jar /usr/src/kgai-java-raw/
COPY run_app.sh /usr/src/kgai-java-raw/
WORKDIR /usr/src/kgai-java-raw
RUN chmod +x run_app.sh
# execute the jar in the container
CMD ["sh", "./run_app.sh"]