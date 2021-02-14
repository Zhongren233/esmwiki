package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.qqbot.entry.IMessageQuickReply;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Lazy
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

    public ObjectNode handle(JsonNode jsonNode) {
        String rawMessage = jsonNode.get("raw_message").textValue();
        log.info("收到讯息:{}", rawMessage);
        if (rawMessage.charAt(0) != '/')
            return null;
        String[] command = rawMessage.split(" ");
        IMessageQuickReply iMessageQuickReply = messageHandlerMap.get(command[0]);
        if (iMessageQuickReply == null) {
            log.info("未找到{}的相关指令", command[0]);
            return null;
        }
        String v = iMessageQuickReply.onMessage(command);
        return mapper.createObjectNode().put("reply", v);
    }

}
