package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.enums.IEventPointReward;
import moe.zr.enums.NormalEventPointReward;
import moe.zr.enums.TourEventPointReward;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.esmwiki.producer.config.EventConfig;
import moe.zr.esmwiki.producer.repository.PointRankingRepository;
import moe.zr.esmwiki.producer.util.RequestUtils;
import moe.zr.pojo.PointRanking;
import moe.zr.pojo.RankingRecord;
import moe.zr.service.PointRankingService;
import org.apache.http.client.methods.HttpPost;
import org.msgpack.type.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static moe.zr.esmwiki.producer.service.impl.SongRankingServiceImpl.getString;

@Service
@Slf4j
public class PointRankingServiceImpl implements PointRankingService {
    private final String uri = "https://saki-server.happyelements.cn/get/events/point_ranking";

    final
    ObjectMapper mapper;
    final
    EsmHttpClient httpClient;
    final
    RequestUtils utils;
    final
    StringRedisTemplate redisTemplate;
    private final EventConfig config;
    final
    PointRankingRepository pointRankingRepository;

    public PointRankingServiceImpl(RequestUtils utils, EsmHttpClient httpClient, ObjectMapper mapper, StringRedisTemplate redisTemplate, EventConfig eventConfig, PointRankingRepository pointRankingRepository) {
        this.utils = utils;
        this.httpClient = httpClient;
        this.mapper = mapper;
        this.redisTemplate = redisTemplate;
        this.config = eventConfig;
        this.pointRankingRepository = pointRankingRepository;
    }

    public List<PointRanking> getPointRankings(Integer page) throws IllegalBlockSizeException, ExecutionException, BadPaddingException, IOException {
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        JsonNode rankingRecord = getRankingRecord(page);
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, PointRanking.class);
        return mapper.readValue(rankingRecord.get("ranking").toString(), javaType);
    }

    public JsonNode getRankingRecord(Integer page) throws IOException, BadPaddingException, IllegalBlockSizeException, ExecutionException {
        HttpPost httpPost = utils.buildHttpRequest(uri, initContent(page));
        Value execute = httpClient.executeAsMessagepack(httpPost);
        return mapper.readTree(execute.toString());
    }

    public JsonNode getRankingRecord(EventRankingNavigationType type) throws IOException, BadPaddingException, IllegalBlockSizeException, ExecutionException {
        HttpPost httpPost = utils.buildHttpRequest(uri, initContent(type));
        Value execute = httpClient.executeAsMessagepack(httpPost);
        return mapper.readTree(execute.toString());
    }

    @Override
    public List<RankingRecord> getRankingRecords() throws BadPaddingException, IOException, IllegalBlockSizeException, ExecutionException {
        ArrayList<RankingRecord> pointRankingRecords = new ArrayList<>();
        for (EventRankingNavigationType value : EventRankingNavigationType.values()) {
            JsonNode node = getRankingRecord(value);
            SongRankingServiceImpl.bathParseRanking(pointRankingRecords, value, node);
        }
        return pointRankingRecords;
    }

    @Override
    public Integer getPointRewardCount(Integer point, Integer startPage) throws IllegalBlockSizeException, ExecutionException, BadPaddingException, IOException {
        int currentPage = startPage;
        int result = (currentPage - 1) * 20;
        do {
            List<PointRanking> ranking = getPointRankings(currentPage);
            int size = ranking.size();
            PointRanking lastRanking = ranking.get(size - 1);
            if (lastRanking.getPoint() >= point) {
                /*
                 * 由于排行榜第一页19人 所以-1
                 */
                result = (currentPage * 20);
                currentPage++;
            } else {
                long count = ranking.stream()
                        .filter(pointRanking -> pointRanking.getPoint() >= point)
                        .count();
                result += count;
                break;
            }
        } while (true);
        if (currentPage == 1)
            return result;
        else
            return result - 1;
    }

    public Integer getPointRewardCount(Integer point) throws ExecutionException, BadPaddingException, IllegalBlockSizeException, IOException {
        String key = "pointRankingService.getPointRewardCount::";
        String s = redisTemplate.opsForValue().get(key + point);
        Integer count;
        if (s == null) {
            Integer countInDB = pointRankingRepository.countByPointGreaterThanEqual(point);
            count = getPointRewardCount(point, countInDB / 20 + 1);
        } else {
            Integer startPage = Integer.valueOf(s);
            count = getPointRewardCount(point, startPage);
        }
        int page = count / 20 + 1;
        redisTemplate.opsForValue().set(
                key + point,
                String.valueOf(page),
                30,
                TimeUnit.MINUTES);
        return count;
    }

    public String batchGetNormalEventPointRewardCount() throws ExecutionException, IllegalBlockSizeException, BadPaddingException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (NormalEventPointReward normalEventPointReward : NormalEventPointReward.values()) {
            stringBuilder.append(normalEventPointReward.getGear()).append("人数:");
            Integer count = getPointRewardCount(normalEventPointReward.getPoint());
            stringBuilder.append(count);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String batchGetTourEventPointRewardCount() throws ExecutionException, IllegalBlockSizeException, BadPaddingException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (TourEventPointReward normalEventPointReward : TourEventPointReward.values()) {
            stringBuilder.append(normalEventPointReward.getGear()).append("人数:");
            Integer count = getPointRewardCount(normalEventPointReward.getPoint());
            stringBuilder.append(count);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
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
            if (str.length > 1) {
                switch (str[1]) {
                    case "now":
                        StringBuilder stringBuilder = new StringBuilder();
                        List<RankingRecord> pointRankingRecords = getRankingRecords();
                        return getString(stringBuilder, pointRankingRecords);
                    case "count":
                        int length = str[2].length();
                        IEventPointReward eventPointReward = null;
                        switch (length) {
                            case 2:
                                eventPointReward = NormalEventPointReward.getEventPointReward(str[2]);
                                break;
                            case 5:
                                eventPointReward = TourEventPointReward.getEventPointReward(str[2]);
                                break;
                        }
                        if (eventPointReward == null) {
                            int point = Integer.parseInt(str[2]);
                            if (point <= 10000) {
                                return "point太小了，不查";
                            }
                            Integer pointRewardCount = getPointRewardCount(point);
                            return point + "pt人数为:" + pointRewardCount;
                        } else {
                            return eventPointReward.getGear() + "人数为:" + getPointRewardCount(eventPointReward.getPoint());
                        }
                    case "batch":
                        if (str.length == 3 && "tour".equals(str[2])) {
                            return batchGetTourEventPointRewardCount();
                        }
                        return batchGetNormalEventPointRewardCount();
                }
            }
            return "/pr {now} |{count}|{batch} {tour}";
        } catch (IndexOutOfBoundsException exception) {
            return "参数长度不正确";
        } catch (IllegalArgumentException illegalArgumentException) {
            return "不正确的参数，支持的参数:\n" +
                    "{一卡} | {二卡} | {三卡} | {四卡} | {满破}\n" +
                    "{第一张一卡} | {第一张二卡} | {第一张三卡} | {第一张四卡} | {第一张满破}\n" +
                    "{第二张一卡} | {第二张二卡} | {第二张三卡} | {第二张四卡} | {第二张满破}";
        } catch (Exception e) {
            log.error("在处理消息时发生了异常，该消息的相关信息 {}", str, e);
            return "为什么会这样呢,相关有用的信息:\n" + e.getMessage();
        }
    }


    @Override
    public String commandPrefix() {
        return "/pr";
    }
}
