FROM amazoncorretto:17-alpine-jdk
COPY out/artifacts/demo_jar/demo.jar demo.jar
ENTRYPOINT ["java","-jar","/demo.jar"]