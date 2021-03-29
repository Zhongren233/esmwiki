package moe.zr.esmwiki.producer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class EventServiceImplTest {
    @Autowired
    EventServiceImpl service;

    void test() throws InterruptedException {
        service.onMessage(new String[2]);
        Thread.sleep(60*1000);
    }
}