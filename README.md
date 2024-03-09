# hw1

### install

unzip
./mvnw clean install

mvnw.cmd clean install

### launch

DB_USERNAME=postgres;DB_URL=jdbc:postgresql://localhost:5432/postgres;DB_PASSWORD=postgres;


plan

- create controller based on spec (/login  /user/register  /user/get/{id})
- add persistence without ORM? what to choose& quarkus-reactive-pg-client but not reactive
- pass hashing
- add it-tests via restassured
- generate postman




TODO

- научиться форматировать лог чтобы он был фиксированной ширины
  2024-03-09 14:55:41 INFO  [com.dim.lon.pck.bla] - messsage 
  2024-03-09 14:55:41 INFO  [com.dim.StartUp    ] - messsage
- Понять нафиг нужен JBossLogManager
- как уюрать кракозябры из лога jdbc 
  Caused by: org.postgresql.util.PSQLException: ������� firstName �� ������� � ���� ResultSet���.
- По нормальному обрабатывать ошибки 