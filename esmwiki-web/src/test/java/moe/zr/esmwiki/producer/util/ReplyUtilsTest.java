package moe.zr.esmwiki.producer.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ReplyUtilsTest {
    @Autowired
    ReplyUtils replyUtils;
    @Test
    void sendMessage() {
        replyUtils.sendMessage("测试 | test message");
    }
}