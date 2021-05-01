package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.esmwiki.producer.config.QuickReplyConfig;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import moe.zr.service.SudoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class QuickReplyServiceImpl {
    final
    ObjectMapper mapper;
    final
    EsmHttpClient httpClient;
    private final Map<String, IMessageQuickReply> messageHandlerMap;
    @Autowired
    SudoService sudoService;

    public QuickReplyServiceImpl(ObjectMapper mapper, ApplicationContext context, EsmHttpClient httpClient, QuickReplyConfig quickReplyConfig) {
        this.mapper = mapper;
        this.httpClient = httpClient;
        messageHandlerMap = quickReplyConfig.getMessageHandlerMap();
    }

    public ObjectNode handle(JsonNode jsonNode) throws JsonProcessingException {
        Message message = mapper.treeToValue(jsonNode, Message.class);
        message.setRawMessage(message.getRawMessage().trim());
        log.info("收到讯息:{}", message.toString());
        String rawMessage = message.getRawMessage();
        if (rawMessage == null || rawMessage.isEmpty()) {
            return null;
        }
        String command = rawMessage.split(" ")[0];
        IMessageQuickReply iMessageQuickReply = messageHandlerMap.get(command);
        String reply = null;
        if (iMessageQuickReply != null) {
            reply = iMessageQuickReply.onMessage(message);
        }
        if ("/sudo".equals(command)) {
            reply = sudoService.onMessage(message);
        }
        if (reply != null) {
            log.info("回复:{}", reply);
            return mapper.createObjectNode().put("reply", reply).put("at_sender", true);
        } else {
            return null;
        }
    }

}
