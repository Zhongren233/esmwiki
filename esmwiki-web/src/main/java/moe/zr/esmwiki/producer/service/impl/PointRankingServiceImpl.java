package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.esmwiki.producer.util.RequestUtils;
import moe.zr.pojo.RankingRecord;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.service.PointRankingService;
import org.apache.http.client.methods.HttpPost;
import org.msgpack.type.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static moe.zr.esmwiki.producer.service.impl.SongRankingServiceImpl.getString;

@Service
public class PointRankingServiceImpl implements PointRankingService, IMessageQuickReply {
    private final String uri = "https://saki-server.happyelements.cn/get/events/point_ranking";

    final
    ObjectMapper mapper;
    final
    EsmHttpClient httpClient;
    final
    RequestUtils utils;

    public PointRankingServiceImpl(RequestUtils utils, EsmHttpClient httpClient, ObjectMapper mapper) {
        this.utils = utils;
        this.httpClient = httpClient;
        this.mapper = mapper;
    }


    public JsonNode getRankingRecord(Integer page) throws IOException, BadPaddingException, IllegalBlockSizeException, ExecutionException, InterruptedException {
        HttpPost httpPost = utils.buildHttpRequest(uri, initContent(page));
        Value execute = httpClient.executeAsMessagepack(httpPost);
        return mapper.readTree(execute.toString());
    }

    public JsonNode getRankingRecord(EventRankingNavigationType type) throws IOException, BadPaddingException, IllegalBlockSizeException, ExecutionException, InterruptedException {
        HttpPost httpPost = utils.buildHttpRequest(uri, initContent(type));
        Value execute = httpClient.executeAsMessagepack(httpPost);
        return mapper.readTree(execute.toString());
    }

    @Override
    public List<RankingRecord> getRankingRecords() throws BadPaddingException, IOException, IllegalBlockSizeException, ExecutionException, InterruptedException {
        ArrayList<RankingRecord> pointRankingRecords = new ArrayList<>();
        for (EventRankingNavigationType value : EventRankingNavigationType.values()) {
            JsonNode node = getRankingRecord(value);
            SongRankingServiceImpl.bathParseRanking(pointRankingRecords, value, node);
        }
        return pointRankingRecords;
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
            List<RankingRecord> pointRankingRecords = getRankingRecords();
            return getString(stringBuilder, pointRankingRecords);
        } catch (Exception e) {
            e.printStackTrace();
            return "为什么会这样呢";
        }
    }

    @Override
    public String commandPrefix() {
        return "/pr";
    }
}
