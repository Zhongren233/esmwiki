package moe.zr.esmwiki.producer.service.impl;

import moe.zr.service.CharacterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CharacterServiceImplTest {
    @Autowired
    CharacterService characterService;
    @Test

    void test() {
        LocalDate now = LocalDate.now();
        LocalDate of = LocalDate.of(2021, 4, 20);
        Period until = now.until(of);
        System.out.println(of.toEpochDay() - now.toEpochDay());
    }

    @Test
    void query() {
        System.out.println(characterService.queryBirthDay());
    }
}