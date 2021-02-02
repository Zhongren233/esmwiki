package moe.zr.esmwiki.producer.daq.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import moe.zr.esmwiki.producer.config.DAQConfig;
import moe.zr.pojo.RankingRecord;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;

@Service
public class PointRankingDAQImpl {
    final
    DAQConfig config;

    final
    ObjectMapper mapper;

    final
    CloseableHttpClient httpClient;

    public PointRankingDAQImpl(DAQConfig config, ObjectMapper mapper, CloseableHttpClient httpClient) {
        this.config = config;
        this.mapper = mapper;
        this.httpClient = httpClient;
    }

    public RankingRecord recordRanking() {
        return null;
    }

    private HttpPost buildRequest() {
        return null;
    }
}
