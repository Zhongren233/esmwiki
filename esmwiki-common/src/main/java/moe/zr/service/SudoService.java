package moe.zr.service;

import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;

public interface SudoService {
    String onMessage(String[] str);

    default String onMessage(Message message) {
        String[] s = message.getRawMessage().split(" ");
        return onMessage(s);
    }

    String commandPrefix();
}
