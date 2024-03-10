# hw1

монолит, без реактивщины, без ОРМ, без оптимизации БД, ручная накатка схемы, генерация рест апи через openapi

### install

- mvn clean install
- docker compose up
- run sql scripts:
    - src/main/resources/db/01_schema-init.sql
    - src/main/resources/db/02_data-init.sql

docker build -f Dockerfile.jvm -t otus-highload-hw1:latest .
docker images
     
### launch

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

TODO

- научиться форматировать лог чтобы он был фиксированной ширины
  2024-03-09 14:55:41 INFO  [com.dim.lon.pck.bla] - messsage
  2024-03-09 14:55:41 INFO  [com.dim.StartUp    ] - messsage
- Понять нафиг нужен JBossLogManager
- как уюрать кракозябры из лога jdbc
  Caused by: org.postgresql.util.PSQLException: ������� firstName �� ������� � ���� ResultSet���.
- По нормальному обрабатывать ошибки
- Засунуть инициализацию БД внутрь образа