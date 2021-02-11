package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import moe.zr.qqbot.entry.IMessageQuickReply;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BotServiceImpl {
    final
    ObjectMapper mapper;
    private final Map<String, IMessageQuickReply> messageHandlerMap = new HashMap<>();

    public BotServiceImpl(ObjectMapper mapper, ApplicationContext context) {
        Map<String, IMessageQuickReply> beansOfType = context.getBeansOfType(IMessageQuickReply.class);
        beansOfType.forEach((k, v) -> messageHandlerMap.put(v.commandPrefix(), v));
        System.out.println(messageHandlerMap);
        this.mapper = mapper;
    }

    public ObjectNode handle(JsonNode jsonNode) {
        String rawMessage = jsonNode.get("raw_message").textValue();
        System.out.println(rawMessage);
        if (rawMessage.charAt(0) != '/') return null;
        String[] command = rawMessage.split(" ");
        IMessageQuickReply iMessageQuickReply = messageHandlerMap.get(command[0]);
        if (iMessageQuickReply == null) return null;

        return mapper.createObjectNode().put("reply", iMessageQuickReply.onMessage(command))
                .put("at_sender", true);
    }
}
