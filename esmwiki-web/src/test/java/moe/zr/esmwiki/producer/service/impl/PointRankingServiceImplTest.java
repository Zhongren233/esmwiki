package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.service.PointRankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;

@SpringBootTest
class PointRankingServiceImplTest {
    @Autowired
    PointRankingService service;
    @Test
    public void testRecord() throws IllegalBlockSizeException, ParseException, BadPaddingException, IOException {
        JsonNode rankingRecord = service.getRankingRecord(EventRankingNavigationType.R1000);
        System.out.println(rankingRecord);
    }
}