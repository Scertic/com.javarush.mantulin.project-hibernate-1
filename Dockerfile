FROM tomcat:9-jdk8

# Удалим дефолтные приложения
RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем WAR в webapps
COPY target/rpg-hibernate-1.0.war /usr/local/tomcat/webapps/ROOT.war

# Порт, на котором будет работать Tomcat
EXPOSE 8080

# Tomcat сам стартует
