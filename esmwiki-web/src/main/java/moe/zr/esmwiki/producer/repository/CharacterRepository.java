package moe.zr.esmwiki.producer.repository;

import moe.zr.entry.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {
    List<Character> findByBirthMonthOrderByBirthday(String birthMonth);

    List<Character> findByBirthday(String birthDay);

    default List<Character> findThisAndNextMonthBirthCharacter() {
        LocalDate now = LocalDate.now();
        Month month = now.getMonth();
        Month nextMonth = month.plus(1);
        String queryMonth;
        if (month.getValue() < 10) {
            queryMonth = "0" + month.getValue() + "月";
        } else {
            queryMonth = month.getValue() + "月";
        }
        String queryNextMonth;
        if (nextMonth.getValue() < 10) {
            queryNextMonth = "0" + nextMonth.getValue() + "月";
        } else {
            queryNextMonth = nextMonth.getValue() + "月";
        }
        return findByBirthMonthOrderByBirthday(queryMonth, queryNextMonth);

    }

    @Query(value = "{$or:[{birthMonth:?0},{birthMonth:?1}]}",sort = "{birthday:1}")
    List<Character> findByBirthMonthOrderByBirthday(String month, String nextMonth);
}
