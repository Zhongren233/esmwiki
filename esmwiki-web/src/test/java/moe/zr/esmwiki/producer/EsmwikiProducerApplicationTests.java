package moe.zr.esmwiki.producer;

import moe.zr.esmwiki.producer.config.EventConfig;
import moe.zr.esmwiki.producer.config.QuickReplyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EsmwikiProducerApplicationTests {
    @Autowired
    EventConfig eventConfig;
    @Autowired
    QuickReplyConfig quickReplyConfig;
    @Test
    void context() {
        System.out.println(eventConfig);
        System.out.println(quickReplyConfig.getMessageHandlerMap());
    }

}
