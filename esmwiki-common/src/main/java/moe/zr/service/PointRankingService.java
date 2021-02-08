package moe.zr.service;

import com.fasterxml.jackson.databind.JsonNode;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.pojo.RankingRecord;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PointRankingService {
    JsonNode getRankingRecord(Integer page) throws IOException, BadPaddingException, IllegalBlockSizeException, ParseException, ExecutionException, InterruptedException;

    JsonNode getRankingRecord(EventRankingNavigationType type) throws IOException, BadPaddingException, IllegalBlockSizeException, ParseException, ExecutionException, InterruptedException;

    List<RankingRecord> getRankingRecords() throws BadPaddingException, IOException, IllegalBlockSizeException, ParseException, ExecutionException, InterruptedException;
}
