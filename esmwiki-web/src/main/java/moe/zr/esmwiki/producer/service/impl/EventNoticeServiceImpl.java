package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.config.EventConfig;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.qqbot.entry.IMessageQuickReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@EnableScheduling
public class EventNoticeServiceImpl implements IMessageQuickReply {
    final
    EventConfig eventConfig;
    private LocalDateTime endDateTime;
    final
    ReplyUtils utils;

    public EventNoticeServiceImpl(EventConfig eventConfig, ReplyUtils utils) {
        this.eventConfig = eventConfig;
        endDateTime = eventConfig.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(8).plusHours(10);
        this.utils = utils;
    }

    @Scheduled(cron = "0 59 7,15,20 * * ? ")
    public void checkEndTime() {
        Duration between = getDuration();
        long hours = between.toHours();
        long days = between.toDays();
        if (hours <= 0) {
            return;
        }
        if (days == 0) {
            utils.sendGroupPostingMessage("距离活动结束还有" + days + "天(" + hours + ")小时");
        } else {
            utils.sendGroupPostingMessage("距离活动结束还有" + hours + "小时!!!!!!!!!!!!!!!");
        }
    }

    private Duration getDuration() {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, endDateTime);
    }

    public String getCountDownString() {
        Duration duration = getDuration();
        long hours = duration.toHours();
        long days = duration.toDays();
        if (hours == 0) {
            return "没救了 等死吧";
        }
        if (hours < 0) {
            return "你醒了？活动结束了";
        }
        if (days != 0) {
            return "距离活动结束还有" + days + "天(" + hours + ")小时";
        } else {
            return "距离活动结束还有" + hours + "小时!!!!!!!!!!!!!!!";
        }
    }

    @Override
    public String onMessage(String[] str) {
        return getCountDownString();
    }

    @Override
    public String commandPrefix() {
        return "/event";
    }
}
