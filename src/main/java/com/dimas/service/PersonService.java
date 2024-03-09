package com.dimas.service;

import com.dimas.domain.PersonCreate;
import com.dimas.domain.entity.Person;
import com.dimas.domain.mapper.PersonMapper;
import com.dimas.exception.ApiLoginFailedException;
import com.dimas.persistence.PersonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.dimas.util.SecurityUtil.encrypt;
import static com.dimas.util.SecurityUtil.validPassword;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public Person findById(UUID id) {
        return personRepository.getById(id);
    }

    public String login(UUID id, String password) {
        var person = personRepository.getById(id);
        log.info("Person is found={}", person);
        if (!validPassword(person.getPassword(), password)) {
            throw new ApiLoginFailedException("Неверный пароль");
        }
        return person.getId().toString();//todo return token, replace to auth service
    }

    public Person findByName(String name) {
        return personRepository.getByName(name);
    }

    public Person create(PersonCreate request) {
        var person = personMapper.map(request);
        person.setPassword(encrypt(request.getPassword()));
        return personRepository.create(person);
    }

}
