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

public interface SongRankingService extends IMessageQuickReply {
    JsonNode getSongRankingRecord(Integer page) throws IOException, BadPaddingException, IllegalBlockSizeException, ParseException, ExecutionException, InterruptedException;

    JsonNode getSongRankingRecord(EventRankingNavigationType type) throws IOException, BadPaddingException, IllegalBlockSizeException, ParseException, ExecutionException, InterruptedException;

    List<RankingRecord> getSongRankingRecords() throws BadPaddingException, IOException, IllegalBlockSizeException, ParseException, ExecutionException, InterruptedException;
}
