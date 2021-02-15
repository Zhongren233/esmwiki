package moe.zr.esmwiki.producer.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class PointRankingServiceImplTest {
    @Autowired
    PointRankingServiceImpl service;
    @Test
    void getRankingRecord() throws BadPaddingException, InterruptedException, IOException, ExecutionException, IllegalBlockSizeException {
        System.out.println("service.getRankingRecord(1) = " + service.getRankingRecord(1));
    }

    @Test
    void getCount() throws InterruptedException, ExecutionException, BadPaddingException, IllegalBlockSizeException, IOException {
        Integer count = service.getPointRewardCount(600 * 10000);
        System.out.println(count);
    }

    @Test
    void testOnMessage(){
        String command = "/pr count 二卡";
        String s = service.onMessage(command.split(" "));
        System.out.println(s);
    }
}