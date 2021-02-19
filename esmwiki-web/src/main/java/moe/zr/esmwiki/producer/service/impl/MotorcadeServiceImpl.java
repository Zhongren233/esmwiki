package moe.zr.esmwiki.producer.service.impl;

import moe.zr.qqbot.entry.IMessageQuickReply;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MotorcadeServiceImpl implements IMessageQuickReply {
    final
    StringRedisTemplate template;
    final
    SimpleDateFormat simpleDateFormat;

    private final String key = "motorcade";


    public MotorcadeServiceImpl(StringRedisTemplate template, SimpleDateFormat simpleDateFormat) {
        this.template = template;
        this.simpleDateFormat = simpleDateFormat;
    }

    public boolean addMotorcade(String str) {
        ZSetOperations<String, String> stringStringZSetOperations = template.opsForZSet();
        Long aLong = stringStringZSetOperations.zCard(key);
        String format = new SimpleDateFormat("(dd日 HH:mm)").format(new Date());
        Boolean add = stringStringZSetOperations.add(key, str + format, -System.currentTimeMillis());
        if (aLong != null && aLong > 10) {
            stringStringZSetOperations.removeRange(key, 10, aLong);
        }
        if (add == null) {
            add = Boolean.FALSE;
        }
        return add;
    }

    @Override
    public String onMessage(String[] str) {
        if (str.length > 1) {
            ArrayList<String> strings = new ArrayList<>(Arrays.asList(str));
            strings.remove(0);
            if (addMotorcade(strings.toString())) {
                return "ok";
            } else {
                return "好像发生了错误";
            }
        }
        Set<String> strings = Objects.requireNonNull(template.opsForZSet().range(key, 0, 10));
        return parseString(strings);
    }

    private String parseString(Set<String> strings) {
        if (strings.isEmpty()) {
            return "啊哦，目前并没有车牌呢";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(simpleDateFormat.format(new Date()));
        stringBuilder.append("的车牌信息:");

        for (String string : strings) {
            stringBuilder.append("\n");
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }


    @Override
    public String commandPrefix() {
        return "/car";
    }
}
