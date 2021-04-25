package moe.zr.esmwiki.producer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.qqbot.entry.SendMessage;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
@Configurable
@Component
@Slf4j
public class ReplyUtils {
    @Value("${reply.address}")
    private String uri;
    final
    ObjectMapper mapper;
    final
    EsmHttpClient httpClient;

    public ReplyUtils(ObjectMapper mapper, EsmHttpClient httpClient) {
        this.mapper = mapper;
        this.httpClient = httpClient;
    }

    public void sendMessage(String message) {
        sendMessage(new SendMessage().setMessage(message));
    }

    public void sendMessage(String message, Long groupId) {
        sendMessage(new SendMessage().setMessage(message).setGroupId(groupId));
    }

    private void sendMessage(SendMessage sendMessage) {
        HttpPost post = new HttpPost(uri+"/send_msg");
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        StringEntity entity;
        try {
            String string = mapper.writeValueAsString(sendMessage);
            entity = new StringEntity(string, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            log.error("发送消息时出现异常:", e);
            return;
        }
        entity.setContentType("application/json");
        post.setEntity(entity);
        try {
            httpClient.executeNoResponse(post);
        } catch (ExecutionException | InterruptedException e) {
            log.error("发送消息时出现异常", e);
            e.printStackTrace();
        }
    }

    /**
     * 用来群发消息
     * @param message 要群发的信息
     */
    public void sendGroupPostingMessage(String message) {
        HttpPost httpPost = new HttpPost(uri + "/get_group_list");
        JsonNode jsonNode;
        try {
            jsonNode = httpClient.executeAsJsonNode(httpPost);
        } catch (ExecutionException | InterruptedException | IOException e) {
            log.error("获取群组信息时出现异常");
            e.printStackTrace();
            return;
        }
        JsonNode data = jsonNode.get("data");
        for (JsonNode datum : data) {
            long group_id = datum.get("group_id").asLong();
            sendMessage(message, group_id);
        }
    }
}
