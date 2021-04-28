package moe.zr.esmwiki.producer.service.impl;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("开始检查生日");
        String query = DateTimeFormatter.ofPattern("MM月dd日").format(LocalDate.now().plusDays(1));
        List<Character> byBirthday = characterRepository.findByBirthday(query);
        if (!byBirthday.isEmpty()) {
            String message = null;
            switch (byBirthday.size()) {
                case 1:
                    message = "明天是 " + byBirthday.get(0).getName() + " 的生日，下单的生日套就要到了，超love～";
                    break;
                case 2:
                    message = "明天是 " + byBirthday.get(0).getName() + " 和 " + byBirthday.get(1).getName() + " 的生日，无论哪个都好love～，都好喜欢，大好き♪";
                    break;
            }
            log.info(message);
            replyUtils.sendGroupPostingMessage(message);
        } else {
            log.info("明天没人过生日");
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
            if (l == 0) {
                stringBuilder.append("今天是 ").append(character.getName()).append(" 的生日！开哪张卡的矿路呢～都超级love啊～");
            }
            if (l > 0) {
                stringBuilder.append("距离 ").append(character.getName()).append(" 的生日还有 ").append(l).append(" 天");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String queryBirthDay(String birthMonth) {
        if (birthMonth.length() == 2) {
            birthMonth = "0" + birthMonth;
        }
        if (birthMonth.length() != 3) {
            return "不正确的格式，示例：/birthday 01月";
        }
        if (!birthMonth.endsWith("月")) {
            return "不正确的格式，示例：/birthday 01月";
        }

        if (birthMonth.startsWith("10") || birthMonth.startsWith("11") || birthMonth.startsWith("12") || birthMonth.startsWith("0")) {
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
