package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.hekk.Friend;
import moe.zr.esmwiki.producer.config.EventConfig;
import moe.zr.esmwiki.producer.repository.BindUserProfileRepository;
import moe.zr.pojo.BindUserProfile;
import moe.zr.pojo.PointRanking;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import moe.zr.service.FriendService;
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
    final
    EventConfig config;
    final
    FriendService friendService;

    public BindServiceImpl(StalkerServiceImpl stalkerService, StringRedisTemplate redisTemplate, BindUserProfileRepository repository, EventConfig config, Random random, FriendService friendService) {
        this.random = random;
        this.stalkerService = stalkerService;
        this.redisTemplate = redisTemplate;
        this.repository = repository;
        this.config = config;
        this.friendService = friendService;
    }

    /**
     * 写的什么抽象代码 我受不了了
     */
    @Override
    public String onMessage(String[] str) {
        int length = str.length;
        Long qqNumber = Long.valueOf(str[str.length - 1]);
        if (repository.findBindUserProfileByQqNumber(qqNumber) != null) {
            return "你已经绑定过了！如需解绑请联系开发者";
        }
        if ("-friend".equals(str[2])) {
            try {
                Friend friend = friendService.findUserIdByQuery(str[1]);
                Integer uid = friend.getUid();
                String key = "BindService:" + qqNumber + "|" + uid;
                String randomName = redisTemplate.opsForValue().get(key);
                if (randomName != null) {
                    if (randomName.equals(friend.getNickname())) {
                        return bind(qqNumber, friend.getUid());
                    } else {
                        return "重新进行绑定，请将游戏昵称设定为" + generalRandomCode(key) + "后，再次输入此命令";
                    }
                } else {
                    return "开始进行绑定，请将游戏昵称设定为" + generalRandomCode(key) + "后，再次输入此命令";
                }
            } catch (Exception e) {
                return e.getMessage();
            }
        } else {
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
                        if (randomName != null) {
                            if (randomName.equals(realTime.getUserProfile().getName())) {
                                return bind(qqNumber, realTime.getUserId());
                            } else {
                                String randomCode = generalRandomCode(key);
                                return "重新进行绑定，请将游戏昵称设定为" + randomCode + "后，输入/bind " + randomCode + " -friend";
                            }
                        }
                        String randomCode = generalRandomCode(key);
                        return "开始进行绑定，请将游戏昵称设定为" + randomCode + "后，输入/bind " + randomCode + " -friend";
                    }
                default:
                    return "好像格式不太正确，示例：/bind {userId} -id | /bind {userName} -friend | /bind {userName}";
            }

        }
    }

    private String bind(Long qqNumber, Integer userId) {
        BindUserProfile bindUserProfile = new BindUserProfile();
        bindUserProfile.setQqNumber(qqNumber);
        bindUserProfile.setUserId(userId);
        repository.insert(bindUserProfile);
        return "绑定成功";
    }

    private String generalRandomCode(String key) {
        int randInt = random.nextInt(89999) + 10000;
        String randomName = String.valueOf(randInt);
        redisTemplate.opsForValue().set(key, randomName, 5, TimeUnit.MINUTES);
        return randomName;
    }

    @Override
    public String onMessage(Message message) {
        if (config.getIsUnAvailable())
            return "绑定功能暂不可用";
        String s = message.getRawMessage() + " " + message.getUserId();
        String[] split = s.split(" ");
        if (split.length==2) {
            return "绑定请参考文档：https://github.com/Zhongren233/esmwiki/blob/master/README.md";
        }
        return onMessage(split);
    }

    @Override
    public String commandPrefix() {
        return "/bind";
    }
}
