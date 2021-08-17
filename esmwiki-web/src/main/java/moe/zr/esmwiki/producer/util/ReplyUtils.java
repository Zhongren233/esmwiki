package moe.zr.esmwiki.producer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    private HttpPost basicPost(String path, ObjectNode objectNode) {
        HttpPost post = new HttpPost(uri + path);
        post.setEntity(basicEntity(objectNode));

        return post;
    }

    private HttpPost basicPost(String path) {
        return new HttpPost(uri + path);
    }

    private StringEntity basicEntity(ObjectNode content) {
        StringEntity entity = new StringEntity(content.toString(), StandardCharsets.UTF_8);
        entity.setContentType("application/json");
        return entity;
    }

    public ReplyUtils(ObjectMapper mapper, EsmHttpClient httpClient) {
        this.mapper = mapper;
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        this.httpClient = httpClient;
    }

    public void sendMessage(String message) {
        sendMessage(new SendMessage().setMessage(message));
    }

    public void sendMessage(String message, Long groupId) {
        sendMessage(new SendMessage().setMessage(message).setGroupId(groupId));
    }

    public void renameGroupCard(Long groupId, Long userId, String groupCard) {
        HttpPost post = new HttpPost(uri + "/set_group_card");
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("group_id", groupId);
        objectNode.put("user_id", userId);
        objectNode.put("card", groupCard);

        StringEntity entity = new StringEntity(objectNode.toString(), StandardCharsets.UTF_8);
        entity.setContentType("application/json");
        post.setEntity(entity);
        try {
            httpClient.executeNoResponse(post);
        } catch (ExecutionException | InterruptedException e) {
            log.error("发送消息时出现异常", e);
            e.printStackTrace();
        }
    }

    public String getPermission(Long groupId, Long userId) {
        HttpPost post = basicPost("/get_group_member_info");
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("group_id", groupId);
        objectNode.put("user_id", userId);
        post.setEntity(basicEntity(objectNode));
        try {
            JsonNode jsonNode = httpClient.executeAsJsonNode(post);
            System.out.println(jsonNode);
            return jsonNode.get("data").get("role").asText();
        } catch (ExecutionException | InterruptedException e) {
            log.error("发送消息时出现异常", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("解析消息时出现异常", e);
            e.printStackTrace();
        }
        return null;
    }

    public ArrayNode getGroupMemberDetailList(Long groupId) {
        HttpPost post = basicPost("/get_group_member_list", mapper.createObjectNode().put("group_id", groupId));
        try {
            JsonNode jsonNode = httpClient.executeAsJsonNode(post);
            System.out.println(jsonNode);
            return (ArrayNode) jsonNode.get("data");
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Long> getGroupMemberList(Long groupId) {
        HttpPost post = basicPost("/get_group_member_list", mapper.createObjectNode().put("group_id", groupId));
        try {
            JsonNode jsonNode = httpClient.executeAsJsonNode(post);
            System.out.println(jsonNode);
            ArrayNode node = (ArrayNode) jsonNode.get("data");
            return node.findValues("user_id").stream().map(JsonNode::asLong).collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            log.error("发送消息时出现异常", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("解析消息时出现异常", e);
            e.printStackTrace();
        }
        return null;
    }

    private void sendMessage(SendMessage sendMessage) {
        HttpPost post = new HttpPost(uri + "/send_msg");
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
     *
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
