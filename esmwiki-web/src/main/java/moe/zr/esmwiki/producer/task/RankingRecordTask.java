package moe.zr.esmwiki.producer.task;

import lombok.extern.slf4j.Slf4j;
import moe.zr.enums.EventType;
import moe.zr.esmwiki.producer.repository.PointRankingRecordRepository;
import moe.zr.esmwiki.producer.repository.ScoreRankingRecordRepository;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.pojo.PointRankingRecord;
import moe.zr.pojo.RankingRecord;
import moe.zr.pojo.SongRankingRecord;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.service.PointRankingService;
import moe.zr.service.SongRankingService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@EnableScheduling
@Configuration
@Slf4j
public class RankingRecordTask implements IMessageQuickReply {
    private boolean flag = false;
    private EventType eventType = EventType.UNIT;
    private final DateFormat dateTimeInstance = DateFormat.getDateTimeInstance();
    final static String cron = "0 */1 * * * ?";

    final
    PointRankingService pointRankingService;
    final
    PointRankingRecordRepository pointRankingRecordRepository;
    final
    ScoreRankingRecordRepository scoreRankingRecordRepository;
    final
    SongRankingService songRankingService;
    final
    ReplyUtils replyUtils;
    final
    SimpleDateFormat simpleDateFormat;


    public RankingRecordTask(PointRankingService pointRankingService, PointRankingRecordRepository pointRankingRecordRepository, ScoreRankingRecordRepository scoreRankingRecordRepository, SongRankingService songRankingService, ReplyUtils replyUtils, SimpleDateFormat simpleDateFormat) {
        this.pointRankingService = pointRankingService;
        this.pointRankingRecordRepository = pointRankingRecordRepository;
        this.scoreRankingRecordRepository = scoreRankingRecordRepository;
        this.songRankingService = songRankingService;
        this.replyUtils = replyUtils;
        this.simpleDateFormat = simpleDateFormat;
    }

    @Scheduled(cron = cron)
    private void task() {
        if (flag) {
            batchGetPointRankingRecord();
            batchGetSongRankingRecord();
        }
    }

    private void batchGetPointRankingRecord() {
        try {
            ArrayList<PointRankingRecord> pointRankingRecords = new ArrayList<>();
            List<RankingRecord> rankingRecords = pointRankingService.getRankingRecords();
            rankingRecords.forEach(
                    a -> pointRankingRecords.add(new PointRankingRecord(a))
            );
            pointRankingRecordRepository.insert(pointRankingRecords);
            log.debug("成功获取");
        } catch (IOException | BadPaddingException | IllegalBlockSizeException | ParseException | ExecutionException | InterruptedException | RuntimeException e) {
            exceptionHandle(e);
        }
    }

    private void batchGetSongRankingRecord() {
        try {
            ArrayList<SongRankingRecord> songRankingRecords = new ArrayList<>();
            songRankingService.getSongRankingRecords().forEach(
                    a -> songRankingRecords.add(new SongRankingRecord(a))
            );
            scoreRankingRecordRepository.insert(songRankingRecords);
            log.debug("成功获取");
        } catch (IOException | BadPaddingException | IllegalBlockSizeException | ParseException | ExecutionException | InterruptedException | RuntimeException e) {
            exceptionHandle(e);
        }
    }

    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void dailyReport() {
        String message = "今天的活动信息" +
                "\n" + "\n" +
                sendTodayEventPointRankingInfo() +
                "\n" +
                sendTodayEventPointRewardInfo();
        log.info(message);
        replyUtils.sendMessage(message);
    }

    private String sendTodayEventPointRankingInfo() {
        return pointRankingService.onMessage("/pr now".split(" "));
    }

    private String sendTodayEventPointRewardInfo() {
        try {
            if (EventType.TOUR.equals(eventType)) {
                return pointRankingService.batchGetTourEventPointRewardCount();
            } else {
                return pointRankingService.batchGetNormalEventPointRewardCount();
            }
        } catch (Exception e) {
            exceptionHandle(e);
            return "获取档位人数出错,可用的信息:" + e.getMessage();
        }
    }

    private void exceptionHandle(Exception e) {
        e.printStackTrace();
        replyUtils.sendMessage("我罢工了");
        replyUtils.sendMessage(e.getMessage());
        log.error("", e);
        flag = false;
    }

    private final Timer timer = new Timer();
    private Date scheduled = null;

    @Override
    public String onMessage(String[] str) {
        if (str.length >= 2) {
            switch (str[1]) {
                case "status":
                    String s = "正在运行:" + flag;
                    if (scheduled != null) {
                        s += "即将运行:";
                        s += DateFormat.getDateTimeInstance().format(scheduled);
                    }
                    return s;
                case "settask":
                    if (scheduled != null) {
                        return "已有任务:" + dateTimeInstance.format(scheduled);
                    }
                    if (str.length == 2) {
                        return "没有参数";
                    }
                    try {
                        scheduled = simpleDateFormat.parse(str[2]);
                        log.info("成功获取到时间:{}", scheduled);
                    } catch (ParseException e) {
                        return "格式不正确 yyyyMMdd HH:mm";
                    }
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            scheduled = null;
                            flag = true;
                        }
                    }, scheduled);
                    return DateFormat.getDateTimeInstance().format(scheduled);
                case "settype":
                    if (str.length == 2) {
                        return "没有参数";
                    }
                    EventType eventType = EventType.getEventType(str[2]);
                    if (eventType == null) {
                        return "格式不正确,支持的参数 {巡演} | {组合} | {洗牌}";
                    }
                    this.eventType = eventType;
                    log.info("设置活动类型为:{}", eventType.getType());
                    return this.eventType.getType();
                case "now":
                    return DateFormat.getDateTimeInstance().format(new Date());
                case "on":
                    this.flag = true;
                    return "ok";
            }
        }
        return "/task {status} | {settask} yyyyMMdd HH:mm | {on} | {settype} {type} ";
    }

    @Override
    public String commandPrefix() {
        return "/task";
    }
}
