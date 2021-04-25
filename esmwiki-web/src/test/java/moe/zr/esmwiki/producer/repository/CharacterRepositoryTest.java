package moe.zr.esmwiki.producer.repository;

import moe.zr.entry.Character;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        List<Character> byBirthMonth = characterRepository.findByBirthMonthOrderByBirthday(query);
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

    @Test
    void findThisAndNextMonthBirthCharacter() {
        characterRepository.findThisAndNextMonthBirthCharacter().forEach(System.out::println);
    }

    @Test
    void findByBirthMon() {
        characterRepository.findByBirthMonthOrderByBirthday("05月").forEach(System.out::println);

    }
}