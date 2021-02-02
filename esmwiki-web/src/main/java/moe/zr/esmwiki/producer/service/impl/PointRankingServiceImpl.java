package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.esmwiki.producer.util.RequestUtils;
import moe.zr.service.PointRankingService;
import org.apache.http.client.methods.HttpPost;
import org.msgpack.type.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class PointRankingServiceImpl implements PointRankingService {
    private final String uri = "https://saki-server.happyelements.cn/get/events/point_ranking";

    final
    ObjectMapper mapper;
    final
    EsmHttpClient httpClient;
    final
    RequestUtils utils;

    public PointRankingServiceImpl(RequestUtils utils, EsmHttpClient httpClient, ObjectMapper mapper, SimpleDateFormat simpleDateFormat) {
        this.utils = utils;
        this.httpClient = httpClient;
        this.mapper = mapper;
    }


    public JsonNode getRankingRecord(Integer page) throws IOException, BadPaddingException, IllegalBlockSizeException {
        HttpPost httpPost = utils.buildHttpRequest(uri, initContent(page));
        Value execute = httpClient.execute(httpPost);
        return mapper.readTree(execute.toString());
    }

    public JsonNode getRankingRecord(EventRankingNavigationType type) throws IOException, BadPaddingException, IllegalBlockSizeException {
        HttpPost httpPost = utils.buildHttpRequest(uri, initContent(type));
        Value execute = httpClient.execute(httpPost);
        return mapper.readTree(execute.toString());
    }


    private String initContent(int page) {
        return utils.basicRequest() + "&page=" + page;
    }

    private String initContent(EventRankingNavigationType type) {
        return utils.basicRequest() + "&event_ranking_navigation_type_id=" + type.getRank();
    }

}
