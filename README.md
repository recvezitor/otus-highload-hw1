# hw1

* монолит
* без реактивщины
* без ОРМ
* без оптимизации БД

### Инструкция

* запустить `docker compose up` из папки `docker`
* импортировать постман коллекцию `OTUS highload hw1.postman_collection.json`
* прогнать по очереди запросы:
    - Register
    - Login
    - GetById

### install

mvn clean install
docker build -f docker/Dockerfile.jvm -t otus-highload-hw1:latest .
docker images

to regenerate openapi: mvn clean install -P openapi

### launch

all together:

cd docker
docker compose up

force update
cd docker
docker-compose pull
docker-compose up --force-recreate --build -d
docker image prune -f

via IDEA:
DB_USERNAME=postgres;DB_URL=jdbc:postgresql://localhost:5432/postgres;DB_PASSWORD=postgres;
-Dquarkus.profile=prod

via docker:
docker run -i --rm -p 8080:8080 -e DB_URL=jdbc:postgresql://host.docker.internal:5432/postgres -e DB_USERNAME=postgres -e DB_PASSWORD=postgres otus-highload-hw1:latest

### publish

docker tag otus-highload-hw1:latest recvezitor/otus-highload-hw1:latest
docker login -> recvezitor/password
docker push recvezitor/otus-highload-hw1:latest

### TODO

- использовать uuid с сортировкой