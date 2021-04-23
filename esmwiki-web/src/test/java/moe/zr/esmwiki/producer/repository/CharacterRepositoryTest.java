package moe.zr.esmwiki.producer.repository;

import moe.zr.entry.Character;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CharacterRepositoryTest {
    @Autowired
    CharacterRepository characterRepository;
    @Test
    void findByBirthMonthContains() {
        LocalDate now = LocalDate.now();
        int monthValue = now.getMonthValue();
        String query;
        if (monthValue < 10) {
            query = "0" + monthValue + "月";
        }else {
            query = monthValue + "月";
        }
        List<Character> byBirthMonth = characterRepository.findByBirthMonth(query);
        for (Character character : byBirthMonth) {
            System.out.println(character);
        }
    }

    @Test
    void findByBirthday() {
        LocalDate localDate = LocalDate.now().plusDays(4);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日");
        String query = formatter.format(localDate);
        List<Character> byBirthday = characterRepository.findByBirthday(query);
        byBirthday.forEach(System.out::println);
    }
}