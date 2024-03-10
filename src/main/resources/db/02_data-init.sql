set search_path to otus_highload;

insert into otus_highload.person (first_name, second_name, birthdate, biography, city, created_at, updated_at, password)
values ('name1', 'secondName1', '2010-01-01', 'text text', 'Vladivostok', now(), null, '123');