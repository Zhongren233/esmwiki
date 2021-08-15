package moe.zr.esmwiki.producer.service.impl;

import moe.zr.service.DAQService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DAQServiceImplTest {
    @Autowired
    DAQService daqService;
    @Test
    void saveAllRanking() throws InterruptedException {
        daqService.saveAllRanking();
        Thread.sleep(360*1000);
    }
}