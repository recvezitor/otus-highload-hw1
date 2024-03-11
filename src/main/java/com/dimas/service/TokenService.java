package com.dimas.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

//Here we could have jwt implementation but left it simple for now
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TokenService {

    public String generate(UUID id, Map<String, Object> details) {
        Random random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

}
