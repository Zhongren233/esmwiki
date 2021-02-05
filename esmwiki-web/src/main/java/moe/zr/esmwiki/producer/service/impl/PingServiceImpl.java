package moe.zr.esmwiki.producer.service.impl;

import moe.zr.qqbot.entry.IMessageQuickReply;
import org.springframework.stereotype.Service;

@Service
public class PingServiceImpl implements IMessageQuickReply {

    @Override
    public String onMessage(String[] str) {
        return "？";
    }

    @Override
    public String commandPrefix() {
        return "/ping";
    }
}
