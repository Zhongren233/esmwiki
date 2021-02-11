package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.qqbot.entry.IMessageQuickReply;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements IMessageQuickReply {

    final
    EsmHttpClient httpClient;

    public EventServiceImpl(EsmHttpClient httpClient) {
        this.httpClient = httpClient;
    }



    @Override
    public String onMessage(String[] str) {
        return null;
    }

    @Override
    public String commandPrefix() {
        return "/event";
    }
}
