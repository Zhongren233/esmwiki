package moe.zr.esmwiki.producer.service.impl;

import lombok.extern.slf4j.Slf4j;
import moe.zr.entry.hekk.PointRanking;
import moe.zr.entry.hekk.ScoreRanking;
import moe.zr.entry.hekk.UserProfile;
import moe.zr.esmwiki.producer.repository.PointRankingRepository;
import moe.zr.esmwiki.producer.repository.ScoreRankingRepository;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.service.StalkerService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StalkerServiceImpl implements StalkerService, IMessageQuickReply {
    final
    PointRankingRepository pointRankingRepository;
    final
    ScoreRankingRepository scoreRankingRepository;

    public StalkerServiceImpl(PointRankingRepository pointRankingRepository, ScoreRankingRepository scoreRankingRepository) {
        this.pointRankingRepository = pointRankingRepository;
        this.scoreRankingRepository = scoreRankingRepository;
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

    /**
     * /stk {userArg} {option}
     */
    @Override
    public String onMessage(String[] str) {
        int length = str.length;
        if (length < 2) {
            return "/stk {userName}";
        }
        String userArg;
        String option = null;
        switch (length) {
            case 3:
                option = str[2];
            case 2:
                userArg = str[1];
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

    private String getReturnString(Integer userId) {
        Optional<PointRanking> optionalPointRanking = getPointRanking(userId);
        Optional<ScoreRanking> optionalScoreRanking = getScoreRanking(userId);
        StringBuilder stringBuilder = new StringBuilder();
        if (optionalPointRanking.isPresent()) {
            PointRanking pointRanking = optionalPointRanking.get();
            UserProfile userProfile = pointRanking.getUserProfile();
            stringBuilder
                    .append("昵称:").append(userProfile.getName()).append("\n")
                    .append("id:").append(pointRanking.getUserId()).append("\n")
                    .append("活动点数排名:").append(pointRanking.getRank()).append("\n")
                    .append("活动点数:").append(pointRanking.getPoint()).append("\n");
            optionalScoreRanking.ifPresent(scoreRanking -> {
                stringBuilder.append("活动歌曲排名:").append(scoreRanking.getRank()).append("\n");
                stringBuilder.append("活动歌曲分数:").append(scoreRanking.getPoint()).append("\n");
            });
            return stringBuilder.toString();
        } else {
            return "没有查询到相关记录";
        }
    }

    private Integer getUserId(String userArg, String option) {
        Integer userId;
        if ("-id".equals(option)) {
            try {
                userId = Integer.valueOf(userArg);
            } catch (NumberFormatException e) {
                throw new RuntimeException("这不是一个有效数字");
            }
        } else {
            PointRanking pointRanking = new PointRanking();
            pointRanking.setUserProfile(new UserProfile().setName(userArg));
            List<PointRanking> all = pointRankingRepository.findAll(Example.of(pointRanking));
            if (all.size() != 1) {
                throw new RuntimeException(multiResult(all));
            } else {
                userId = all.get(0).getUserId();
            }
        }
        return userId;
    }

    private String multiResult(List<PointRanking> all) {
        StringBuilder stringBuilder = new StringBuilder();
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
        return stringBuilder.toString();
    }

    @Override
    public String commandPrefix() {
        return "/stk";
    }
}
