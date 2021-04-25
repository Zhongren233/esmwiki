package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.Character;
import moe.zr.esmwiki.producer.repository.CharacterRepository;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.service.CharacterService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Service
@EnableScheduling
public class CharacterServiceImpl implements CharacterService, IMessageQuickReply {
    final
    CharacterRepository characterRepository;

    public CharacterServiceImpl(CharacterRepository characterRepository, ReplyUtils replyUtils) {
        this.characterRepository = characterRepository;
        this.replyUtils = replyUtils;
    }

    final
    ReplyUtils replyUtils;

    @Override
    @Scheduled(cron = "0 0 19 * * ? ")
    public void checkBirthDay() {
        String query = DateTimeFormatter.ofPattern("MM月dd日").format(MonthDay.now());
        List<Character> byBirthday = characterRepository.findByBirthday(query);
        if (!byBirthday.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("明天是 ");
            switch (byBirthday.size()) {
                case 1:
                    stringBuilder.append(byBirthday.get(0).getName());
                    stringBuilder.append(" 的生日");
                    break;
                case 2:
                    stringBuilder.append(byBirthday.get(0).getName());
                    stringBuilder.append(" 和 ");
                    stringBuilder.append(byBirthday.get(1).getName());
                    stringBuilder.append("的生日");
                    break;
            }
            replyUtils.sendGroupPostingMessage(stringBuilder.toString());
        }


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
        if (birthMonth.length()==2) {
            birthMonth = "0" + birthMonth;
        }
        if (birthMonth.length()!=3) {
            return "不正确的格式，示例：/birthday 01月";
        }
        if (!birthMonth.endsWith("月")) {
            return "不正确的格式，示例：/birthday 01月";
        }

        if (birthMonth.startsWith("11")||birthMonth.startsWith("12")||birthMonth.startsWith("0")) {
            List<Character> byBirthMonth = characterRepository.findByBirthMonthOrderByBirthday(birthMonth);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("在 ").append(birthMonth).append(" 过生日的小偶像有:\n");
            for (Character character : byBirthMonth) {
                stringBuilder.append(character.getName());
                stringBuilder.append("\t");
                stringBuilder.append(character.getBirthday());
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
        return "众所周知，一年有12月";
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
