package moe.zr.service;

import com.fasterxml.jackson.databind.JsonNode;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.pojo.RankingRecord;
import moe.zr.qqbot.entry.IMessageQuickReply;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PointRankingService extends IMessageQuickReply {
    JsonNode getRankingRecord(Integer page) throws IOException, BadPaddingException, IllegalBlockSizeException, ParseException, ExecutionException, InterruptedException;

    JsonNode getRankingRecord(EventRankingNavigationType type) throws IOException, BadPaddingException, IllegalBlockSizeException, ParseException, ExecutionException, InterruptedException;

    List<RankingRecord> getRankingRecords() throws BadPaddingException, IOException, IllegalBlockSizeException, ParseException, ExecutionException, InterruptedException;

    Integer getPointRewardCount(Integer point, Integer startPage) throws IllegalBlockSizeException, ExecutionException, InterruptedException, BadPaddingException, IOException;

    Integer getPointRewardCount(Integer point) throws InterruptedException, ExecutionException, BadPaddingException, IllegalBlockSizeException, IOException;

    String batchGetNormalEventPointRewardCount() throws InterruptedException, ExecutionException, IllegalBlockSizeException, BadPaddingException, IOException;

    String batchGetTourEventPointRewardCount() throws InterruptedException, ExecutionException, IllegalBlockSizeException, BadPaddingException, IOException;
}
