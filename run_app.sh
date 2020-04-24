#!/bin/bash

if [[ -z "${APP_VERSION}" ]]; then
  echo "APP_VERSION is not set or undefined"
else
  echo "APP_VERSION is set to: ${APP_VERSION}"
  java -Dspring.profiles.active=default -jar "kgai-java-raw-${APP_VERSION}.jar"
fi
