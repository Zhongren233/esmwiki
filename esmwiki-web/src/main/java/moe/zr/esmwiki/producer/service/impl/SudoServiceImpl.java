package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.config.QuickReplyConfig;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import moe.zr.service.SudoService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
public class SudoServiceImpl implements SudoService {
    private final Map<String, IMessageQuickReply> messageHandlerMap;

    public SudoServiceImpl(QuickReplyConfig quickReplyConfig) {
        messageHandlerMap = quickReplyConfig.getMessageHandlerMap();
    }

    @Override
    public String onMessage(String[] str) {
        int length = str.length;
        if (length == 1) {
            return "喵呜";
        }
        IMessageQuickReply iMessageQuickReply = messageHandlerMap.get(str[1]);
        if (iMessageQuickReply == null) {
            return "没有找到对应指令";
        }
        System.out.println(Arrays.toString(str));
        String[] strings = new String[10];
        System.arraycopy(str, 1, strings, 0, str.length - 1);
        strings = Arrays.copyOf(strings, str.length-1);
        System.out.println(Arrays.toString(strings));
        return iMessageQuickReply.onMessage(strings);
    }

    @Override
    public String onMessage(Message message) {
        Long groupId = message.getGroupId();
        Long userId = message.getUserId();
        if (groupId == 773891409|| userId==732713726) {
            String[] s = message.getRawMessage().split(" ");
            return onMessage(s);
        }
        return null;
    }

    @Override
    public String commandPrefix() {
        return "/sudo";
    }
}
