package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.esmwiki.producer.util.ParseUtils;
import moe.zr.esmwiki.producer.util.RequestUtils;
import moe.zr.pojo.RankingRecord;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.service.SongRankingService;
import org.apache.http.client.methods.HttpPost;
import org.msgpack.type.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
@Service
public class SongRankingServiceImpl implements SongRankingService, IMessageQuickReply {
    private final String uri = "https://saki-server.happyelements.cn/get/events/score_ranking";

    final
    ObjectMapper mapper;
    final
    EsmHttpClient httpClient;
    final
    RequestUtils utils;

    public SongRankingServiceImpl(RequestUtils utils, EsmHttpClient httpClient, ObjectMapper mapper) {
        this.utils = utils;
        this.httpClient = httpClient;
        this.mapper = mapper;
    }

    @Override
    public JsonNode getSongRankingRecord(Integer page) throws IOException, BadPaddingException, IllegalBlockSizeException, ExecutionException, InterruptedException {
        HttpPost httpPost = utils.buildHttpRequest(uri, initContent(page));
        Value execute = httpClient.execute(httpPost);
        return mapper.readTree(execute.toString());
    }

    @Override
    public JsonNode getSongRankingRecord(EventRankingNavigationType type) throws IOException, BadPaddingException, IllegalBlockSizeException, ExecutionException, InterruptedException {
        HttpPost httpPost = utils.buildHttpRequest(uri, initContent(type));
        Value execute = httpClient.execute(httpPost);
        return mapper.readTree(execute.toString());
    }

    @Override
    public List<RankingRecord> getSongRankingRecords() throws BadPaddingException, IOException, IllegalBlockSizeException, ExecutionException, InterruptedException {
        ArrayList<RankingRecord> rankingRecords = new ArrayList<>();
        for (EventRankingNavigationType value : EventRankingNavigationType.values()) {
            JsonNode node = getSongRankingRecord(value);
            bathParseRanking(rankingRecords, value, node);
        }
        return rankingRecords;
    }

    static void bathParseRanking(ArrayList<RankingRecord> rankingRecords, EventRankingNavigationType value, JsonNode node) {
        RankingRecord record = ParseUtils.getRecord(node, 0);
        record.setRank(value.getRank());
        rankingRecords.add(record);
        if (value == EventRankingNavigationType.R1) {
            RankingRecord r10 = ParseUtils.getRecord(node, 9);
            r10.setRank(10);
            rankingRecords.add(r10);
        }
    }

    private String initContent(int page) {
        return utils.basicRequest() + "&page=" + page;
    }

    private String initContent(EventRankingNavigationType type) {
        return utils.basicRequest() + "&event_ranking_navigation_type_id=" + type.getRank();
    }

    @Override
    public String onMessage(String[] str) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            List<RankingRecord> rankingRecords = getSongRankingRecords();
            return getString(stringBuilder, rankingRecords);
        } catch (Exception e) {
            e.printStackTrace();
            return "为什么会这样呢";
        }
    }

    static String getString(StringBuilder stringBuilder, List<RankingRecord> rankingRecords) {
        stringBuilder.append(new Date());
        stringBuilder.append("的活动积分榜\n");
        for (RankingRecord rankingRecord : rankingRecords) {
            stringBuilder.append("rank");
            stringBuilder.append(rankingRecord.getRank());
            stringBuilder.append(" = ");
            stringBuilder.append(rankingRecord.getPoint());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String commandPrefix() {
        return "/sr";
    }
}
