package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.Character;
import moe.zr.esmwiki.producer.repository.CharacterRepository;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Service
@EnableScheduling
public class CharacterServiceImpl implements CharacterService, IMessageQuickReply {
    final
    CharacterRepository characterRepository;

    public CharacterServiceImpl(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Override
//    @Scheduled(cron = "")
    public void checkBirthDay() {

    }

    @Override
    public String queryBirthDay() {
        List<Character> characters = characterRepository.findThisAndNextMonthBirthCharacter();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日");
        LocalDate now = LocalDate.now();
        StringBuilder stringBuilder = new StringBuilder();
        for (Character character : characters) {
            String birthday = character.getBirthday();
            TemporalAccessor parse = formatter.parse(birthday);
            MonthDay from = MonthDay.from(parse);
            LocalDate localDate = from.atYear(now.getYear());
            long l = localDate.toEpochDay() - now.toEpochDay();
            if (l==0) {
                stringBuilder.append("今天是 ").append(character.getName()).append(" 的生日！");
            }
            if (l>0) {
                stringBuilder.append("距离 ").append(character.getName()).append(" 的生日还有 ").append(l).append(" 天");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String queryBirthDay(String birthMonth) {
        return null;
    }

    @Override
    public String onMessage(String[] str) {
        switch (str.length) {
            case 1:
                return queryBirthDay();
            case 2:
                return queryBirthDay(str[1]);
            default:
                return "意料外的参数长度: " + str.length;
        }
    }

    @Override
    public String commandPrefix() {
        return "/birthday";
    }
}
