package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.esmwiki.producer.config.EventConfig;
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
    final
    EventConfig config;


    public SongRankingServiceImpl(RequestUtils utils, EsmHttpClient httpClient, ObjectMapper mapper, EventConfig config) {
        this.utils = utils;
        this.httpClient = httpClient;
        this.mapper = mapper;
        this.config = config;
    }

    @Override
    public JsonNode getSongRankingRecord(Integer page) throws IOException, BadPaddingException, IllegalBlockSizeException, ExecutionException {
        HttpPost httpPost = utils.buildHttpRequest(uri, initContent(page));
        Value execute;
        try {
            execute = httpClient.executeAsMessagepack(httpPost);
        } catch (ExecutionException e) {
            execute = httpClient.executeAsMessagepack(httpPost);
        }
        return mapper.readTree(execute.toString());
    }

    @Override
    public JsonNode getSongRankingRecord(EventRankingNavigationType type) throws IOException, BadPaddingException, IllegalBlockSizeException, ExecutionException {
        HttpPost httpPost = utils.buildHttpRequest(uri, initContent(type));
        Value execute;
        try {
            execute = httpClient.executeAsMessagepack(httpPost);
        } catch (ExecutionException e) {
            execute = httpClient.executeAsMessagepack(httpPost);
        }
        return mapper.readTree(execute.toString());
    }

    @Override
    public List<RankingRecord> getSongRankingRecords() throws BadPaddingException, IOException, IllegalBlockSizeException, ExecutionException {
        ArrayList<RankingRecord> pointRankingRecords = new ArrayList<>();
        for (EventRankingNavigationType value : EventRankingNavigationType.values()) {
            JsonNode node = getSongRankingRecord(value);
            bathParseRanking(pointRankingRecords, value, node);
        }
        return pointRankingRecords;
    }

    static void bathParseRanking(ArrayList<RankingRecord> songRankingRecords, EventRankingNavigationType value, JsonNode node) {
        RankingRecord record = ParseUtils.getRecord(node, 0);
        record.setRank(value.getRank());
        songRankingRecords.add(record);
        if (value == EventRankingNavigationType.R1) {
            RankingRecord r2 = ParseUtils.getRecord(node, 1);
            RankingRecord r3 = ParseUtils.getRecord(node, 2);
            RankingRecord r10 = ParseUtils.getRecord(node, 9);
            r2.setRank(2);
            r3.setRank(3);
            r10.setRank(10);
            songRankingRecords.add(r2);
            songRankingRecords.add(r3);
            songRankingRecords.add(r10);
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
        if (config.getIsUnAvailable()) {
            return "功能暂不可用";
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            List<RankingRecord> pointRankingRecords = getSongRankingRecords();
            return getString(stringBuilder, pointRankingRecords);
        } catch (Exception e) {
            e.printStackTrace();
            return "为什么会这样呢";
        }
    }

    static String getString(StringBuilder stringBuilder, List<? extends RankingRecord> songRankingRecords) {
        stringBuilder.append(new Date());
        stringBuilder.append("的活动榜\n");
        for (RankingRecord rankingRecord : songRankingRecords) {
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
