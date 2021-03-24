package moe.zr.esmwiki.producer.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import moe.zr.enums.EventStatus;
import moe.zr.enums.EventType;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Configuration
@Getter
@Setter
@Slf4j
public class EventConfig implements IMessageQuickReply {
    private Date date;
    private EventType eventType;
    private final DateFormat dateTimeInstance = DateFormat.getDateTimeInstance();
    private Timer timer = new Timer();
    private EventStatus status;

    final
    StringRedisTemplate redisTemplate;
    final
    SimpleDateFormat simpleDateFormat;
    final
    ReplyUtils replyUtils;
    final
    ObjectMapper mapper;


    public EventConfig(SimpleDateFormat simpleDateFormat, ReplyUtils replyUtils, ObjectMapper mapper, StringRedisTemplate redisTemplate) {
        this.simpleDateFormat = simpleDateFormat;
        this.replyUtils = replyUtils;
        this.mapper = mapper;
        this.redisTemplate = redisTemplate;
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        String eventConfig = redisTemplate.opsForValue().get("Event:Config");
        String s = "没有配置信息，请设置配置";
        if (eventConfig != null) {
            s = "成功从redis中获得配置:{}";
            JsonNode jsonNode = mapper.readTree(eventConfig);
            log.info(s,jsonNode);
            String type = jsonNode.get("event_type").asText();
            this.date = simpleDateFormat.parse(jsonNode.get("date").asText());
            eventType = EventType.getEventType(type);
            status = EventStatus.valueOf(jsonNode.get("status").asText());
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    status = EventStatus.Open;
                }
            }, this.date);
        }
        replyUtils.sendMessage(s);
        log.info(this.toString());
    }

    public void setDate(Date date) {
        this.date = date;
        saveConfig();
    }

    private void saveConfig() {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("date", simpleDateFormat.format(date));
        objectNode.put("event_type", eventType.getType());
        objectNode.put("status", status.name());
        redisTemplate.opsForValue().set("Event:Config", objectNode.toString());
    }

    @Override
    public String onMessage(String[] str) {
        if (str.length > 2) {
            switch (str[1]) {
                case "date":
                    if (date != null) {
                        return "已有任务:" + dateTimeInstance.format(date);
                    }
                case "sudo.date":
                    timer.cancel();
                    this.timer = new Timer();
                    try {
                        this.setDate(simpleDateFormat.parse(str[2]));
                        log.info("成功获取到时间:{}", date);
                    } catch (ParseException e) {
                        return "格式不正确 yyyyMMdd HH:mm";
                    }
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            setDate(null);
                            status = EventStatus.Open;
                        }
                    }, date);
                    return dateTimeInstance.format(date);
                case "type":
                    EventType eventType = EventType.getEventType(str[2]);
                    if (eventType == null) {
                        return "格式不正确,支持的参数 {巡演} | {组合} | {洗牌}";
                    }
                    this.eventType = eventType;
                    log.info("设置活动类型为:{}", eventType.getType());
                    return this.eventType.getType();
                case "status":
                    try {
                        status = EventStatus.valueOf(str[2]);
                        return status.toString();
                    } catch (IllegalArgumentException e) {
                        return ("Unexpected value: " + str[2]);
                    }
                default:
                    return ("Unexpected value: " + str[1]);
            }
        }
        return toString();
    }


    @Override
    public String onMessage(Message message) {
        if (message.getGroupId() == 773891409) {
            String[] split = message.getRawMessage().split(" ");
            return onMessage(split);
        }
        return null;
    }

    @Override
    public String toString() {
        return "EventConfig{" +
                "date=" + date +
                ", eventType=" + eventType +
                ", status=" + status +
                '}';
    }

    public void setStatus(EventStatus status) {
        this.status = status;
        saveConfig();
    }

    public void setUnavailable() {
        if (EventStatus.Open.equals(this.getStatus())) {
            setStatus(EventStatus.End);
            return;
        }

        if (EventStatus.CountingEnd.equals(this.getStatus())) {
            setStatus(EventStatus.Announce);
        }

    }

    public boolean getIsOpen() {
        return status.equals(EventStatus.Open);
    }

    public boolean getIsAnnounce() {
        return status.equals(EventStatus.Announce);
    }

    public boolean getIsEnd() {
        return status.equals(EventStatus.End);
    }

    public boolean getIsCountingEnd() {
        return status.equals(EventStatus.CountingEnd);
    }

    public boolean getIsUnAvailable() {
        return status.equals(EventStatus.End) || status.equals(EventStatus.Announce);
    }
    @Override
    public String commandPrefix() {
        return "/config";
    }
}
