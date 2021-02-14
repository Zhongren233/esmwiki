package moe.zr.esmwiki.producer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.service.impl.QuickReplyServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bot")
@Slf4j
public class BotController {
    final
    QuickReplyServiceImpl service;
    final
    ObjectMapper mapper;

    public BotController(QuickReplyServiceImpl service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ObjectNode bot(@RequestBody JsonNode node) {
        if (node.get("status") != null)
            return null;
        try {
            return service.handle(node);
        } catch (Exception e) {
            log.error("发生异常", e);
            return mapper.createObjectNode().put("reply", e.getMessage());
        }
    }

}
