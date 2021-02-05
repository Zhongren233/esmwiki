package moe.zr.qqbot.entry;

/**
 * 这玩意尽量 让他在五秒之内返回
 */
public interface IMessageQuickReply {
    String onMessage(String[] str);

    String commandPrefix();
}
