package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Lazy //为什么这会有一个懒加载 神秘
public class QuickReplyServiceImpl {
    final
    ObjectMapper mapper;
    final
    EsmHttpClient httpClient;
    private final Map<String, IMessageQuickReply> messageHandlerMap = new HashMap<>();

    public QuickReplyServiceImpl(ObjectMapper mapper, ApplicationContext context, EsmHttpClient httpClient) {
        Map<String, IMessageQuickReply> beansOfType = context.getBeansOfType(IMessageQuickReply.class);
        System.out.println(beansOfType);
        beansOfType.forEach((k, v) -> messageHandlerMap.put(v.commandPrefix(), v));
        this.mapper = mapper;
        this.httpClient = httpClient;
    }

    public ObjectNode handle(JsonNode jsonNode) throws JsonProcessingException {
        Message message = mapper.treeToValue(jsonNode, Message.class);
        String rawMessage = message.getRawMessage();
        Long id = message.getUserId();
        String type = message.getMessageType();
        switch (type) {
            case "group":
                log.info("收到群{} 用户{}的讯息:{}", message.getGroupId(), id, rawMessage);
                break;
            case "private":
                log.info("收到私聊{}的讯息:{}", id, rawMessage);
        }
        if (rawMessage == null || rawMessage.charAt(0) != '/') return null;
        String[] command = rawMessage.split(" ");
        IMessageQuickReply iMessageQuickReply = messageHandlerMap.get(command[0]);
        if (iMessageQuickReply == null) {
            log.info("未找到{}的相关指令", command[0]);
            return null;
        }
        String v = iMessageQuickReply.onMessage(message);
        return mapper.createObjectNode().put("reply", v);
    }

}
