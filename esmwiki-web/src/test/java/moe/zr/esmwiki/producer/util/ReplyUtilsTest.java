package moe.zr.esmwiki.producer.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class ReplyUtilsTest {
    @Autowired
    ReplyUtils replyUtils;
    @Test
    void sendMessage() {
        replyUtils.sendMessage("测试 | test message");
    }

    @Test
    void groupMessage() {
        replyUtils.sendGroupPostingMessage("测试群发");
    }
}