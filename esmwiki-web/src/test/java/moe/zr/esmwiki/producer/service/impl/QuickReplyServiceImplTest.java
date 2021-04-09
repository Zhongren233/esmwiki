package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuickReplyServiceImplTest {
    @Autowired
    QuickReplyServiceImpl botService;

    @Test
    void sendMessage() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
    }
}