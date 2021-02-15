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
    PointRankingService service;
    @Test
    void getRankingRecord() throws BadPaddingException, InterruptedException, ParseException, IOException, ExecutionException, IllegalBlockSizeException {
        System.out.println("service.getRankingRecord(1) = " + service.getRankingRecord(1));
    }

    @Test
    void getCount() throws InterruptedException, ExecutionException, BadPaddingException, IllegalBlockSizeException, IOException {
        Integer count = service.getCount(350 * 10000);
        System.out.println(count);
    }
}