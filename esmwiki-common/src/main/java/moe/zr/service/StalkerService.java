package moe.zr.service;

import moe.zr.entry.hekk.PointRanking;
import moe.zr.entry.hekk.ScoreRanking;
import moe.zr.qqbot.entry.IMessageQuickReply;

import java.util.Optional;

public interface StalkerService  {
    Optional<PointRanking> getPointRanking(Integer userId);

    Optional<ScoreRanking> getScoreRanking(Integer userId);

}
