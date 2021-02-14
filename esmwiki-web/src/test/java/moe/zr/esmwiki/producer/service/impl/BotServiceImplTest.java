package moe.zr.esmwiki.producer.service.impl;

import moe.zr.qqbot.entry.SendMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BotServiceImplTest {
    @Autowired
    BotServiceImpl botService;

    @Test
    void sendMessage() {
        botService.sendMessage(new SendMessage().setMessage("测试发送信息 test message").setGroupId(773891409));
    }
}