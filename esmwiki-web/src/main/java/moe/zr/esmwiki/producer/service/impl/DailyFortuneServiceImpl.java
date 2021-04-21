package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.util.DailyFortuneProducer;
import moe.zr.pojo.DailyFortune;
import moe.zr.pojo.Fortune;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@EnableScheduling
public class DailyFortuneServiceImpl implements IMessageQuickReply {
    final
    StringRedisTemplate redisTemplate;
    final
    DailyFortuneProducer dailyFortuneProducer;
    private final String redisPrefix = "DailyFortuneService";
    final
    ObjectMapper objectMapper;

    public DailyFortuneServiceImpl(StringRedisTemplate stringRedisTemplate, DailyFortuneProducer dailyFortuneProducer, ObjectMapper objectMapper) {
        this.redisTemplate = stringRedisTemplate;
        this.dailyFortuneProducer = dailyFortuneProducer;
        this.objectMapper = objectMapper;
    }

    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void deleteDailyFortune() {
        log.info("删除今日运势集合");
        redisTemplate.delete(redisPrefix);
    }

    @SneakyThrows
    private String genDailyFortune(Message message) {
        String userId = message.getUserId().toString();
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        String json = hashOperations.get(redisPrefix, userId);
        DailyFortune dailyFortune;
        if (json == null) {
            dailyFortune = dailyFortuneProducer.getDailyFortune();
            hashOperations.put(redisPrefix, userId, objectMapper.writeValueAsString(dailyFortune));
        } else {
            dailyFortune = objectMapper.readValue(json, DailyFortune.class);
        }
        return getReturnString(dailyFortune);
    }

    private String getReturnString(DailyFortune dailyFortune) {
        StringBuilder stringBuilder = new StringBuilder("今日运势:");
        stringBuilder.append(dailyFortune.getLuck().getLuck());
        stringBuilder.append("\n");
        for (Fortune fortune : dailyFortune.getLuckyFortune()) {
            stringBuilder.append("宜").append(fortune.getName()).append(": ").append(fortune.getSuitable()).append("\n");
        }
        for (Fortune fortune : dailyFortune.getUnluckyFortune()) {
            stringBuilder.append("忌").append(fortune.getName()).append(": ").append(fortune.getUnsuitable()).append("\n");
        }
        stringBuilder.append("\n");
        stringBuilder.append("音游推荐朝向：").append(dailyFortune.getDirection().getName());
        return stringBuilder.toString();
    }

    @Override
    public String onMessage(String[] str) {
        return null;
    }

    @Override
    public String onMessage(Message message) {
        return genDailyFortune(message);
    }

    @Override
    public String commandPrefix() {
        return "今日运势";
    }
}
