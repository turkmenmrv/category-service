FROM java:8

MAINTAINER turkmen.mrv@gmail.com

EXPOSE 8081
COPY target/category-0.0.1-SNAPSHOT.jar /category-service.jar

CMD java -jar -Dspring.profiles.active=docker /category-service.jar