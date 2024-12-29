# Родительский образ контейнера с java внутри
FROM eclipse-temurin
# Путь к jar-файлу в качестве аргумента
ARG JAR_FILE=target/*.jar
# Копирование jar-файла в контейнер
COPY ${JAR_FILE} app.jar
# Открытие порта 8080
EXPOSE 8080
# Команда, которая будет запущена при старте контейнера java -jar ./app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]