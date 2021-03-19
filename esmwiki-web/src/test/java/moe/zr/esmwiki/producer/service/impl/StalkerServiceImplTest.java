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
    void test() {
        long l = System.currentTimeMillis();
        StalkerServiceImpl service = (StalkerServiceImpl) stalkerService;
        String message = "/stk 70225704 -id";
        String s = service.onMessage(message.split(" "));
        System.out.println(s);
        System.out.println(System.currentTimeMillis() - l);
    }
}