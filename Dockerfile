FROM eclipse-temurin
VOLUME /tmp
COPY target/*.jar topfive.jar
ENTRYPOINT ["java","-jar","/topfive.jar"]