package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.config.EventConfig;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.qqbot.entry.IMessageQuickReply;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@EnableScheduling
public class EventNoticeServiceImpl implements IMessageQuickReply {
    final
    EventConfig eventConfig;
    private LocalDateTime endDateTime;
    private LocalDateTime startDateTime;
    final
    ReplyUtils utils;

    public EventNoticeServiceImpl(EventConfig eventConfig, ReplyUtils utils) {
        this.eventConfig = eventConfig;
        startDateTime = eventConfig.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        endDateTime = startDateTime.plusDays(8).plusHours(10);
        this.utils = utils;
    }

    @Scheduled(cron = "0 59 7,15,20 * * ? ")
    public void checkEndTime() {
        if (eventConfig.getIsOpen()) {
            Duration between = getEndDuration();
            long hours = between.toHoursPart();
            long days = between.toDaysPart();
            if (hours <= 0) {
                return;
            }
            if (days == 0) {
                utils.sendGroupPostingMessage(MessageFormat.format("距离活动结束还有{0}天{1}小时", days, hours));
            } else {
                utils.sendGroupPostingMessage(MessageFormat.format("距离活动结束还有{0}小时!!!!!!!!!!!!!!!", hours));
            }
        }
    }

    private Duration getEndDuration() {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, endDateTime);
    }

    private Duration getStartDuration() {
        return Duration.between(LocalDateTime.now(), startDateTime);
    }

    public String getCountDownString() {
        if (eventConfig.getIsOpen()) {
            Duration duration = getEndDuration();
            long hours = duration.toHoursPart();
            long days = duration.toDaysPart();
            if (hours == 0) {
                return "没救了 等死吧";
            }
            if (days != 0) {
                return MessageFormat.format("距离活动结束还有{0}天{1}小时", days, hours);
            } else {
                return MessageFormat.format("距离活动结束还有{0}小时!!!!!!!!!!!!!!!", hours);
            }
        }

        if (eventConfig.getIsEnd()) {
            return "你醒了？活动结束了。";
        }

        if (eventConfig.getIsAnnounce()) {
            Duration startDuration = getStartDuration();
            long hours = startDuration.toHoursPart();
            long days = startDuration.toDaysPart();
            if (days != 0) {
                return MessageFormat.format("距离活动开始还有{0}天{1}小时", days, hours);
            } else {
                return MessageFormat.format("距离活动开始还有{0}小时!!!!!!", hours);
            }
        }
        return null;
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
