package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import moe.zr.esmwiki.producer.repository.ScoreRankingRepository;
import moe.zr.pojo.PointRankingRecord;
import moe.zr.service.SongRankingService;
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
class SongPointRankingServiceImplTest {
    @Autowired
    SongRankingService service;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    ScoreRankingRepository rankingRepository;

    @Test
    void getSongRankingRecords() throws BadPaddingException, InterruptedException, ParseException, IOException, ExecutionException, IllegalBlockSizeException {
        service.getSongRankingRecords().forEach(System.out::println);
    }

//    @Test
//    void testAllRecords() throws BadPaddingException, InterruptedException, ParseException, IOException, ExecutionException, IllegalBlockSizeException {
//        int currentPage = 1;
//        int totalPage;
//        do {
//            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
//            JsonNode rankingRecord = service.getSongRankingRecord(currentPage);
//            totalPage = rankingRecord.get("total_pages").asInt();
//            JsonNode ranking = rankingRecord.get("ranking");
//            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, ScoreRanking.class);
//            List<ScoreRanking> rankingList = mapper.readValue(ranking.toString(), javaType);
//            System.out.println(rankingList);
//            rankingRepository.insert(rankingList);
//            currentPage++;
//        } while (currentPage <= totalPage);
//    }
}