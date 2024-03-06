FROM amazoncorretto:17-alpine-jdk
EXPOSE 8090
COPY out/artifacts/demo_jar/demo.jar demo.jar
ENTRYPOINT ["java","-jar","/demo.jar", "--environment=Development"]