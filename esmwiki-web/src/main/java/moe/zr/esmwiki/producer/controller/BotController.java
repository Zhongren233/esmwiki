package moe.zr.esmwiki.producer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import moe.zr.esmwiki.producer.service.impl.BotServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bot")
public class BotController {
    final
    BotServiceImpl service;

    public BotController(BotServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ObjectNode bot(@RequestBody JsonNode node) {
        if (node.get("status") != null)
            return null;
        try {
            return service.handle(node);
        } catch (Exception ignored) {
            return null;
        }
    }

}
