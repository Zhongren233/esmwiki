package moe.zr.service;

import moe.zr.pojo.PointRanking;
import moe.zr.pojo.ScoreRanking;

import java.util.Optional;

public interface StalkerService {
    Optional<PointRanking> getPointRanking(Integer userId);

    Optional<ScoreRanking> getScoreRanking(Integer userId);

    Integer getUserId(String userArg, String option);

    PointRanking getRealTimePointRanking(PointRanking pointRankingInDataBase);

    PointRanking getRealTimePointRanking(Integer userId);

    String getReturnString(Integer userId);

    }
