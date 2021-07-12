FROM maven:3.8.1-jdk-11 AS build

ADD . $MAVEN_HOME

WORKDIR $MAVEN_HOME

RUN mvn clean install -U -DskipTests

FROM tomcat as runtime

COPY --from=build /root/.m2/repository/com/helger/phase4/phase4-peppol-server-webapp/1.3.2-SNAPSHOT /usr/local/tomcat/webapps/phase4

RUN apt update && apt install -y vim certbot

ADD ./init.sh /root/init.sh
ADD ./server.xml /usr/local/tomcat/conf/server.xml

EXPOSE 8080

