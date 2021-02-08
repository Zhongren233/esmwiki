package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.esmwiki.producer.repository.RankingRepository;
import moe.zr.pojo.RankingRecord;
import moe.zr.service.PointRankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class PointPointRankingServiceImplTest {
    @Autowired
    PointRankingService service;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    RankingRepository rankingRepository;

    @Test
    public void testRecord() throws IllegalBlockSizeException, ParseException, BadPaddingException, IOException, ExecutionException, InterruptedException {
        JsonNode rankingRecord = service.getRankingRecord(EventRankingNavigationType.R1000);
        System.out.println(rankingRecord);
    }

    @Test
    void testRecords() throws IllegalBlockSizeException, ParseException, BadPaddingException, IOException, ExecutionException, InterruptedException {
        long l = System.currentTimeMillis();
        List<RankingRecord> rankingRecords = service.getRankingRecords();
        rankingRecords.forEach(System.out::println);
        System.out.println(System.currentTimeMillis() - l);
    }

//    @Test
//    void testAllRecords() throws BadPaddingException, InterruptedException, ParseException, IOException, ExecutionException, IllegalBlockSizeException {
//        int currentPage = 1;
//        int totalPage;
//        do {
//            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
//            JsonNode rankingRecord = service.getRankingRecord(currentPage);
//            totalPage = rankingRecord.get("total_pages").asInt();
//            JsonNode ranking = rankingRecord.get("ranking");
//            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Ranking.class);
//            List<Ranking> rankingList = mapper.readValue(ranking.toString(), javaType);
//            System.out.println(rankingList);
//            rankingRepository.insert(rankingList);
//            currentPage++;
//        } while (currentPage <= totalPage);
//    }
}