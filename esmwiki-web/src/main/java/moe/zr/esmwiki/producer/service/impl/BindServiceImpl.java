package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.repository.BindUserProfileRepository;
import moe.zr.pojo.BindUserProfile;
import moe.zr.pojo.PointRanking;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import moe.zr.service.StalkerService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class BindServiceImpl implements IMessageQuickReply {
    final
    StalkerService stalkerService;
    final
    StringRedisTemplate redisTemplate;
    final
    Random random;
    final
    BindUserProfileRepository repository;

    public BindServiceImpl(StalkerServiceImpl stalkerService, StringRedisTemplate redisTemplate, BindUserProfileRepository repository) {
        random = new Random();
        this.stalkerService = stalkerService;
        this.redisTemplate = redisTemplate;
        this.repository = repository;
    }

    /**
     * 写的什么抽象代码 我受不了了
     */
    @Override
    public String onMessage(String[] str) {
        int length = str.length;
        Long qqNumber = Long.valueOf(str[str.length - 1]);
        switch (length) {
            case 3:
            case 4:
                Integer userId;
                try {
                    userId = stalkerService.getUserId(str[1], str[2]);
                } catch (Exception exception) {
                    return exception.getMessage();
                }
                Optional<PointRanking> optional = stalkerService.getPointRanking(userId);
                if (optional.isPresent()) {
                    PointRanking pointRanking = optional.get();
                    PointRanking realTime = stalkerService.getRealTimePointRanking(pointRanking);
                    String key = "BindService:" + qqNumber + "|" + userId;
                    String randomName = redisTemplate.opsForValue().get(key);
                    if (randomName != null) if (randomName.equals(realTime.getUserProfile().getName())) {
                        BindUserProfile bindUserProfile = new BindUserProfile();
                        bindUserProfile.setQqNumber(qqNumber);
                        bindUserProfile.setUserId(realTime.getUserId());
                        repository.insert(bindUserProfile);
                        return "绑定成功";
                    }
                    return "正在进行绑定，请将游戏昵称设定为" + generalRandomInt(key) + "后，再次输入此命令";
                } else return "没有查询到相关用户呢，QAQ\n如果想通过id绑定，请携带-id\n示例：/bind {userId} -id";
            default:
                return "好像格式不太正确，示例：/bind {userId} -id | /bind {userName}";
        }
    }

    private String generalRandomInt(String key) {
        int randInt = random.nextInt(89999) + 10000;
        String randomName = String.valueOf(randInt);
        redisTemplate.opsForValue().set(key, randomName, 5, TimeUnit.MINUTES);
        return randomName;
    }

    @Override
    public String onMessage(Message message) {
        Long userId = message.getUserId();
        String rawMessage = message.getRawMessage();
        String s = rawMessage + " " + userId;
        String[] split = s.split(" ");
        return onMessage(split);
    }

    @Override
    public String commandPrefix() {
        return "/bind";
    }
}
