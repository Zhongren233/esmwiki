package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import moe.zr.entry.hekk.Friend;
import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.esmwiki.producer.util.RequestUtils;
import moe.zr.service.FriendService;
import org.apache.http.client.methods.HttpPost;
import org.msgpack.type.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

@Service
public class FriendServiceImpl implements FriendService {
    final
    EsmHttpClient httpClient;
    final
    RequestUtils utils;
    final
    ObjectMapper mapper;

    public FriendServiceImpl(EsmHttpClient httpClient, RequestUtils utils, ObjectMapper mapper) {
        this.httpClient = httpClient;
        this.utils = utils;
        this.mapper = mapper;
    }

    @Override
    public Friend findUserIdByQuery(String query) throws BadPaddingException, IllegalBlockSizeException, IOException, ExecutionException {
        ArrayNode arrayNode = getArrayNode(query);
        if (arrayNode.size() == 0) {
            throw new RuntimeException("[错误] 没有找到相关用户");
        }
        if (arrayNode.size()!=1) {
            throw new RuntimeException("[错误] 找到多个用户，建议改名");
        }
        JsonNode jsonNode = arrayNode.get(0);
        return mapper.treeToValue(jsonNode, Friend.class);
    }

    private ArrayNode getArrayNode(String query) throws BadPaddingException, IllegalBlockSizeException, IOException, ExecutionException {
        String uri = "https://saki-server.happyelements.cn/friends/search";
        String encode = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String content = utils.basicRequest() + "&uid=" + encode;
        HttpPost httpPost = utils.buildHttpRequest(uri, content);
        Value value = httpClient.executeAsMessagepack(httpPost);
        JsonNode jsonNode = mapper.readTree(value.toString()).get("search_result");
        return (ArrayNode) jsonNode;
    }
}
