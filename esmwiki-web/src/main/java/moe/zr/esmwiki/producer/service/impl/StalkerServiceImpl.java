package moe.zr.esmwiki.producer.service.impl;

import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.config.EventConfig;
import moe.zr.esmwiki.producer.exception.handler.MultiResultException;
import moe.zr.esmwiki.producer.repository.PointRankingRepository;
import moe.zr.esmwiki.producer.repository.ScoreRankingRepository;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.pojo.PointRanking;
import moe.zr.pojo.ScoreRanking;
import moe.zr.pojo.UserProfile;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import moe.zr.service.PointRankingService;
import moe.zr.service.StalkerService;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
@Slf4j
public class StalkerServiceImpl implements StalkerService, IMessageQuickReply {
    final
    PointRankingRepository pointRankingRepository;
    final
    ScoreRankingRepository scoreRankingRepository;
    final
    PointRankingService pointRankingService;
    final
    ReplyUtils replyUtils;
    final
    StringRedisTemplate redisTemplate;
    private final EventConfig eventConfig;

    public StalkerServiceImpl(PointRankingRepository pointRankingRepository,
                              ScoreRankingRepository scoreRankingRepository,
                              PointRankingService pointRankingService,
                              ReplyUtils replyUtils,
                              StringRedisTemplate redisTemplate, EventConfig eventConfig) {
        this.pointRankingRepository = pointRankingRepository;
        this.scoreRankingRepository = scoreRankingRepository;
        this.pointRankingService = pointRankingService;
        this.replyUtils = replyUtils;
        this.redisTemplate = redisTemplate;
        this.eventConfig = eventConfig;
    }

    @Override
    public Optional<PointRanking> getPointRanking(Integer userId) {
        PointRanking probe = new PointRanking();
        probe.setUserId(userId);
        return pointRankingRepository.findOne(Example.of(probe));

    }

    @Override
    public Optional<ScoreRanking> getScoreRanking(Integer userId) {
        ScoreRanking scoreRanking = new ScoreRanking();
        scoreRanking.setUserId(userId);
        return scoreRankingRepository.findOne(Example.of(scoreRanking));
    }

    private int getStartPage(PointRanking pointRanking) {
        Integer userId = pointRanking.getUserId();
        String key = "Stalker.startPage::" + userId;
        String s = redisTemplate.opsForValue().get(key);
        if (s != null) {
            log.info("成功命中缓存");
            return Integer.parseInt(s);
        }
        return pointRanking.getRank() / 20 + 1;
    }

    @Override
    public PointRanking getRealTimePointRanking(Integer userId) {
        Optional<PointRanking> pointRanking = getPointRanking(userId);
        if (pointRanking.isPresent()) {
            return getRealTimePointRanking(pointRanking.get());
        }
        throw new NoSuchElementException("[错误]似乎还没有记录");
    }

    @Override
    public PointRanking getRealTimePointRanking(PointRanking pointRankingInDataBase) {
        int startPage = getStartPage(pointRankingInDataBase);
        int currentPage = startPage;
        int dir = 1;
        int totalNumberPageGet = 0;
        PointRanking realtime = null;
        do {
            try {
                List<PointRanking> pointRankings = pointRankingService.getPointRankings(currentPage);
                Predicate<PointRanking> pointRankingPredicate = pointRanking1 -> pointRanking1.getUserId().equals(pointRankingInDataBase.getUserId());
                Stream<PointRanking> stream = pointRankings.stream();
                boolean noneMatch = stream.noneMatch(pointRankingPredicate);
                if (noneMatch) {
                    //修改了一下超时设置
                    if (totalNumberPageGet >= 200) {
                        break;
                    }
                    /*
                        想了一下 还是要写个注释
                        这个是根据之前的rank向后翻页
                        直到达到原来的点数还没有翻到人  再向前翻页
                     */
                    PointRanking pointRanking = pointRankings.get(0);
                    if (pointRankingInDataBase.getPoint() >= pointRanking.getPoint()) {
                        currentPage = startPage;
                        dir = -dir;
                        log.info("已到达末尾，向前翻页");
                    }
                    currentPage += dir;
                    ++totalNumberPageGet;
                    log.info("翻取{}页没有发现，继续翻取", currentPage);
                } else {
                    Optional<PointRanking> any = pointRankings.stream().filter(pointRankingPredicate).findAny();
                    if (any.isPresent()) {
                        realtime = any.get();
                        Integer userId = realtime.getUserId();
                        String key = "Stalker.startPage::" + userId;
                        redisTemplate.opsForValue().set(key, String.valueOf(currentPage), 5, TimeUnit.MINUTES);
                    }
                    break;
                }
            } catch (IllegalBlockSizeException | ExecutionException | InterruptedException | BadPaddingException | IOException e) {
                replyUtils.sendMessage("在获取实时PointRanking时发生异常,请参见日志");
                log.error("在获取实时PointRanking时发生异常", e);
            }
        } while (true);
        log.info("实时的PointRanking:{}", realtime);
        return realtime;
    }


    /**
     * /stk {userArg} {option}
     */
    @Override
    public String onMessage(String[] str) {
        if (eventConfig.getIsUnAvailable()) {
            return "当前不是活动时间";
        }
        int length = str.length;
        if (length < 2) {
            return "/stk {userName}  {option}";
        }
        String userArg = str[1];
        String option = null;
        switch (length) {
            case 3:
                option = str[2];
                if ("-rank".equals(option)) {
                    Integer rank = Integer.valueOf(userArg);
                    try {
                        PointRanking pointRankingByRank = getPointRankingByRank(rank);
                        return MessageFormat.format(
                                "昵称:{0}\n" +
                                        "活动排名:{1}\n" +
                                        "活动点数:{2}", pointRankingByRank.getUserProfile().getName(), pointRankingByRank.getRank(), pointRankingByRank.getPoint());
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                }
            case 2:
                Integer userId;
                try {
                    userId = getUserId(userArg, option);
                } catch (RuntimeException exception) {
                    return exception.getMessage();
                }
                return getReturnString(userId);
        }
        return "/stk {userArg} {option}";
    }

    public PointRanking getPointRankingByRank(Integer rank) throws InterruptedException, ExecutionException, BadPaddingException, IllegalBlockSizeException, IOException {
        int page = (rank / 20) + 1;
        List<PointRanking> pointRankings = pointRankingService.getPointRankings(page);
        if (rank < 20) {
            return pointRankings.get(rank - 1);
        } else {
            int index = (rank - 21) % 20;
            return pointRankings.get(index);
        }
    }

    @Override
    public String getReturnString(Integer userId) {
        if (eventConfig.getIsUnAvailable()) {
            return "当前不是活动时间";
        }
        Optional<PointRanking> optionalPointRanking = getPointRanking(userId);
        Optional<ScoreRanking> optionalScoreRanking = getScoreRanking(userId);
        StringBuilder stringBuilder = new StringBuilder();
        if (optionalPointRanking.isPresent()) {
            PointRanking pointRanking = optionalPointRanking.get();
            UserProfile userProfile = pointRanking.getUserProfile();
            if (pointRanking.getPoint() >= 10000) {
                PointRanking realTimePointRanking = getRealTimePointRanking(pointRanking);
                if (realTimePointRanking != null) {
                    pointRanking = realTimePointRanking;
                    userProfile = pointRanking.getUserProfile();
                } else {
                    stringBuilder.append("[警告]非实时Ranking\n");
                }
            }
            stringBuilder
                    .append("昵称:").append(userProfile.getName()).append("\n")
                    .append("活动点数排名:").append(pointRanking.getRank()).append("\n")
                    .append("活动点数:").append(pointRanking.getPoint()).append("\n");
//            optionalScoreRanking.ifPresent(scoreRanking -> {
//                stringBuilder.append("活动歌曲排名:").append(scoreRanking.getRank()).append("\n");
//                stringBuilder.append("活动歌曲分数:").append(scoreRanking.getPoint()).append("\n");
//            });
            return stringBuilder.toString();
        } else {
            return "[错误]没有查询到相关记录";
        }
    }

    public void getReturnImage(Integer userId) {

    }

    @Override
    public Integer getUserId(String userArg, String option) {
        Integer userId;
        if ("-id".equals(option)) {
            try {
                userId = Integer.valueOf(userArg);
            } catch (NumberFormatException e) {
                throw new RuntimeException("[错误]" + userArg + "不是一个有效数字id");
            }
        } else {
            PointRanking pointRanking = new PointRanking();
            pointRanking.setUserProfile(new UserProfile().setName(userArg));
            List<PointRanking> all = pointRankingRepository.findAll(Example.of(pointRanking));
            if (all.isEmpty()) {
                throw new NoSuchElementException("[错误]没有查询到相关记录呢");
            }
            if (all.size() != 1) {
                throw new MultiResultException(multiResult(all));
            } else {
                userId = all.get(0).getUserId();
            }
        }
        return userId;
    }

    private String multiResult(List<PointRanking> all) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = all.size();
        if (size < 20) {
            stringBuilder.append("似乎有多个结果，请按照id查询\n");
            stringBuilder.append("userId|rank|point\n");
            for (PointRanking ranking : all) {
                stringBuilder.append(ranking.getUserId());
                stringBuilder.append("\t");
                stringBuilder.append(ranking.getRank());
                stringBuilder.append("\t");
                stringBuilder.append(ranking.getPoint());
                stringBuilder.append("\n");
            }
        } else {
            stringBuilder.append(String.format("[警告]总共有%d个结果，换个昵称再试吧!", size));
        }
        return stringBuilder.toString();
    }

    @Override
    public String onMessage(Message message) {
        if (message.getGroupId() == 773891409) {
            String[] s = message.getRawMessage().split(" ");
            return onMessage(s);
        }
        return null;
    }

    @Override
    public String commandPrefix() {
        return "/stk";
    }
}
