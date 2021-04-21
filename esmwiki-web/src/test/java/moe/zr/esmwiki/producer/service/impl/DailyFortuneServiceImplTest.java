package moe.zr.esmwiki.producer.service.impl;

import moe.zr.qqbot.entry.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DailyFortuneServiceImplTest {
    @Autowired
    DailyFortuneServiceImpl dailyFortuneService;
    @Test
    void onMessage() {
        Message message = new Message();
        message.setUserId(123456L);
        System.out.println(dailyFortuneService.onMessage(message));
    }
}