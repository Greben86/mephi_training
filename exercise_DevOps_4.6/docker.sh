# сборка контейнера
docker build -t my_server_app .
# запуск контейнера
docker run -d -p 8080:8080 my_server_app