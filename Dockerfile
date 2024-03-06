FROM amazoncorretto:17-alpine-jdk
EXPOSE 8090
EXPOSE 8080
ENV JAVA_ENVIRONMENT=Development
COPY out/artifacts/demo_jar/demo.jar demo.jar
ENTRYPOINT ["java","-jar","/demo.jar", "--environment=Development"]