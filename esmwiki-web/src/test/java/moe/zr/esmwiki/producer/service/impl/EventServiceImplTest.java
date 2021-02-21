package moe.zr.esmwiki.producer.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventServiceImplTest {
    @Autowired
    EventServiceImpl service;

    @Test
    void test() throws  InterruptedException {
        String s = service.onMessage(new String[1]);
        System.out.println(s);
        Thread.sleep(20*1000);
    }

}