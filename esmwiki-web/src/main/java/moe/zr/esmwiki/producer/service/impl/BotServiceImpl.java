package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.SendMessage;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class BotServiceImpl {
    final
    ObjectMapper mapper;
    final
    EsmHttpClient httpClient;
    private final Map<String, IMessageQuickReply> messageHandlerMap = new HashMap<>();

    public BotServiceImpl(ObjectMapper mapper, ApplicationContext context, EsmHttpClient httpClient) {
        Map<String, IMessageQuickReply> beansOfType = context.getBeansOfType(IMessageQuickReply.class);
        beansOfType.forEach((k, v) -> messageHandlerMap.put(v.commandPrefix(), v));
        System.out.println(messageHandlerMap);
        this.mapper = mapper;
        this.httpClient = httpClient;
    }

    public ObjectNode handle(JsonNode jsonNode) {
        String rawMessage = jsonNode.get("raw_message").textValue();
        if (rawMessage.charAt(0) != '/')
            return null;
        String[] command = rawMessage.split(" ");
        IMessageQuickReply iMessageQuickReply = messageHandlerMap.get(command[0]);
        if (iMessageQuickReply == null)
            return null;

        return mapper.createObjectNode().put("reply", iMessageQuickReply.onMessage(command));
    }

    public void sendMessage(SendMessage sendMessage) {
        HttpPost post = new HttpPost("http://localhost:5700/send_msg");
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        StringEntity entity;
        try {
            String string = mapper.writeValueAsString(sendMessage);
            entity = new StringEntity(string, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            log.error("",e);
            return;
        }
        entity.setContentType("application/json");
        post.setEntity(entity);
        try {
            httpClient.executeNoResponse(post);
        } catch (ExecutionException | InterruptedException e) {
            log.error("发送信息出错",e);
            e.printStackTrace();
        }
    }
}
