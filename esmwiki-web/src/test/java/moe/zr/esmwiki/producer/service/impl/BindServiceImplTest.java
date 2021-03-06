package moe.zr.esmwiki.producer.service.impl;

import moe.zr.qqbot.entry.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BindServiceImplTest {
    @Autowired
    BindServiceImpl bindService;
    @Test
    void onMessage() {
        Message message = new Message();
        message.setUserId(73271372L);
        String rawMessage = "/bind";
        message.setRawMessage(rawMessage);
        String s = bindService.onMessage(message);
        System.out.println(s);
    }
}