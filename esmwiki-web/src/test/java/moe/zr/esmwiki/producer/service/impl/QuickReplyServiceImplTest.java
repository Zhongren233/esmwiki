package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    void sendMessage() throws JsonProcessingException {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("raw_message", "ycm");
        objectNode.put("message_type", "group");
        objectNode.put("user_id", 123457);
        objectNode.put("time", System.currentTimeMillis() / 1000);
        System.out.println(botService.handle(objectNode));
    }
}