package moe.zr.esmwiki.producer.service.impl;

import moe.zr.service.StalkerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StalkerServiceImplTest {
    @Autowired
    StalkerService stalkerService;
    @Test
    void testOnMessage() {
        StalkerServiceImpl stalkerService = (StalkerServiceImpl) this.stalkerService;
        String s = "/stk 70001701 -id";
        String s1 = stalkerService.onMessage(s.split(" "));
        System.out.println(s1);
    }
}