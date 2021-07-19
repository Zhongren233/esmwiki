package moe.zr.esmwiki.producer.service.impl;

import moe.zr.service.PointRankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PointRankingServiceImplTest {
    @Autowired
    PointRankingService pointRankingService;
    @Test
    void getRankingRecords() throws IllegalBlockSizeException, BadPaddingException, IOException, ParseException, ExecutionException, InterruptedException {
        pointRankingService.getRankingRecords().forEach(System.out::println);
    }
}