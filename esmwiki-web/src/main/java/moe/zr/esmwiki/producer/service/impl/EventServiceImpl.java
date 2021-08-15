package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.esmwiki.producer.repository.PointRankingRepository;
import moe.zr.esmwiki.producer.repository.ScoreRankingRepository;
import moe.zr.esmwiki.producer.util.CryptoUtils;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.esmwiki.producer.util.RequestUtils;
import moe.zr.pojo.PointRankingTmp;
import moe.zr.pojo.ScoreRankingTmp;
import moe.zr.service.EventService;
import moe.zr.service.PointRankingService;
import moe.zr.service.SongRankingService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.bson.Document;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@EnableAsync
public class EventServiceImpl implements EventService {
    final
    RequestUtils requestUtils;
    final
    CloseableHttpAsyncClient httpClient;
    final
    PointRankingRepository pointRankingRepository;
    final
    ScoreRankingRepository scoreRankingRepository;
    final
    PointRankingService pointRankingService;
    final
    SongRankingService songRankingService;
    final
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    MongoTemplate template;

    private final MongoDatabase esmusic;

    public EventServiceImpl(CloseableHttpAsyncClient eventClient, PointRankingRepository pointRankingRepository, ScoreRankingRepository scoreRankingRepository, PointRankingService pointRankingService, SongRankingService songRankingService, ObjectMapper mapper, RequestUtils requestUtils, StringRedisTemplate stringRedisTemplate, ReplyUtils replyUtils, MongoClient mongoClient) {
        this.httpClient = eventClient;
        this.pointRankingRepository = pointRankingRepository;
        this.scoreRankingRepository = scoreRankingRepository;
        this.pointRankingService = pointRankingService;
        this.songRankingService = songRankingService;
        this.mapper = mapper;
        this.requestUtils = requestUtils;
        this.stringRedisTemplate = stringRedisTemplate;
        this.replyUtils = replyUtils;
        this.esmusic = mongoClient.getDatabase("ESMUSIC");
    }

    final
    ReplyUtils replyUtils;
    final
    ObjectMapper mapper;

    private String initContent(int page) {
        return requestUtils.basicRequest() + "&page=" + page;
    }

    private String initContent(EventRankingNavigationType type) {
        return requestUtils.basicRequest() + "&event_ranking_navigation_type_id=" + type.getRank();
    }

    @Async
    public AsyncResult<Long> saveAllPointRanking() throws BadPaddingException, InterruptedException, ParseException, IOException, ExecutionException, IllegalBlockSizeException, TimeoutException {
        ObjectReader reader = mapper.readerFor(new TypeReference<List<PointRankingTmp>>() {
        });
        long old = System.currentTimeMillis();
        String uri = "https://saki-server.happyelements.cn/get/events/point_ranking";
        JsonNode record = pointRankingService.getRankingRecord(1);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        int totalPages = record.get("total_pages").intValue();
        int eventId = record.get("eventId").intValue();
        CountDownLatch latch = new CountDownLatch(totalPages);
        template.dropCollection(PointRankingTmp.class);
        for (int i = 1; i <= totalPages; i++) {
            HttpPost httpPost = requestUtils.buildHttpRequest(uri, initContent(i));
            httpClient.execute(httpPost, new FutureCallback<>() {
                @Override
                @SneakyThrows
                public void completed(HttpResponse httpResponse) {
                    latch.countDown();
                    JsonNode jsonNode = countDownAndGet(httpResponse);
                    if (httpResponse.getStatusLine().getStatusCode() != 200) {
                        log.warn("状态码不等于200,返回的正文:{}", jsonNode);
                    } else {
                        ArrayNode rankings = (ArrayNode) jsonNode.get("ranking");
                        List<PointRankingTmp> pointRankings = reader.readValue(rankings);
                        pointRankings.forEach(pointRanking -> pointRanking.setEventId(eventId));
                        template.insertAll(pointRankings);
                    }
                }

                @Override
                public void failed(Exception e) {
                    latch.countDown();
                    System.out.println(e.toString());
                }

                @Override
                public void cancelled() {
                    latch.countDown();
                    System.out.println("can");
                }
            });
        }
        if (!latch.await(90, TimeUnit.SECONDS)) {
            log.warn("timeout");
            replyUtils.sendMessage("爬取PointRanking时超时了，呜呜呜");
        }
        timeOutCheck(latch);
        log.info("开始合并pointRanking");
        esmusic.getCollection("pointRankingTmp").aggregate(Arrays.asList(new Document("$unset", "_id"),
                new Document("$merge",
                        new Document("into", "pointRanking")
                                .append("on", "userId")
                                .append("whenMatched", "replace")
                                .append("whenNotMatched", "insert")))).toCollection();
        log.info("pointRanking合并结束");
        return new AsyncResult<>(System.currentTimeMillis() - old);
    }

    @Async
    public AsyncResult<Long> saveAllScoreRanking() throws BadPaddingException, InterruptedException, ParseException, IOException, ExecutionException, IllegalBlockSizeException, TimeoutException {
        ObjectReader reader = mapper.readerFor(new TypeReference<List<ScoreRankingTmp>>() {
        });
        long old = System.currentTimeMillis();
        String uri = "https://saki-server.happyelements.cn/get/events/score_ranking";
        JsonNode record = songRankingService.getSongRankingRecord(1);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        int totalPages = record.get("total_pages").intValue();
        int eventId = record.get("eventId").intValue();
        CountDownLatch latch = new CountDownLatch(totalPages);
        template.dropCollection(ScoreRankingTmp.class);
        for (int i = 1; i <= totalPages; i++) {
            HttpPost httpPost = requestUtils.buildHttpRequest(uri, initContent(i));
            httpClient.execute(httpPost, new FutureCallback<>() {
                @Override
                @SneakyThrows
                public void completed(HttpResponse httpResponse) {
                    latch.countDown();
                    JsonNode jsonNode = countDownAndGet(httpResponse);
                    if (httpResponse.getStatusLine().getStatusCode() != 200) {
                        log.warn("状态码不等于200,返回的正文:{}", jsonNode);
                    } else {
                        JsonNode rankings = jsonNode.get("ranking");
                        List<ScoreRankingTmp> tmps = reader.readValue(rankings);
                        tmps.forEach(scoreRankingTmp -> scoreRankingTmp.setEventId(eventId));
                        template.insertAll(tmps);
                    }
                }

                @Override
                public void failed(Exception e) {
                    latch.countDown();
                    log.error("在爬取时发生异常", e);
                }

                @Override
                public void cancelled() {
                    latch.countDown();
                    System.out.println("cancel");
                }
            });
        }
        if (!latch.await(90, TimeUnit.SECONDS)) {
            log.warn("timeout");
            replyUtils.sendMessage("爬取ScoreRankings时超时了，呜呜呜");
        }
        timeOutCheck(latch);
        log.info("开始合并scoreRanking");
        esmusic.getCollection("scoreRanking").aggregate(Arrays.asList(new Document("$unset", "_id"),
                new Document("$merge",
                        new Document("into", "pointRanking")
                                .append("on", "userId")
                                .append("whenMatched", "replace")
                                .append("whenNotMatched", "insert")))).toCollection();

        log.info("scoreRanking合并结束");
        return new AsyncResult<>(System.currentTimeMillis() - old);
    }

    private void timeOutCheck(CountDownLatch latch) throws InterruptedException, TimeoutException {
        if (!latch.await(90, TimeUnit.SECONDS)) {
            throw new TimeoutException("二次超时，放弃更新数据库！");
        }
        log.info("check ok");
    }


    private JsonNode countDownAndGet(HttpResponse httpResponse) throws IOException, BadPaddingException, IllegalBlockSizeException {
        byte[] bytes = new byte[50 * 1000];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(httpResponse.getEntity().getContent());
        int read = bufferedInputStream.read(bytes);
        bytes = Arrays.copyOf(bytes, read);
        bufferedInputStream.close();
        Value read1 = new MessagePack().read(CryptoUtils.decrypt(bytes));
        return mapper.readTree(read1.toString());
    }

}
