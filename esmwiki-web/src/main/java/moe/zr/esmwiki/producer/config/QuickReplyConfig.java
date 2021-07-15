package moe.zr.esmwiki.producer.config;

import moe.zr.qqbot.entry.IMessageQuickReply;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class QuickReplyConfig {
    private final HashMap<String, IMessageQuickReply> messageHandlerMap = new HashMap<>();

    public QuickReplyConfig(ApplicationContext context) {
        Map<String, IMessageQuickReply> beansOfType = context.getBeansOfType(IMessageQuickReply.class);
        System.out.println(beansOfType);
        beansOfType.forEach((k, v) -> {
            String[] split = v.commandPrefix().split("\\|");
            for (String s : split) {
                messageHandlerMap.put(s, v);
            }
        });
    }

    public HashMap<String, IMessageQuickReply> getMessageHandlerMap() {
        return messageHandlerMap;
    }
}
