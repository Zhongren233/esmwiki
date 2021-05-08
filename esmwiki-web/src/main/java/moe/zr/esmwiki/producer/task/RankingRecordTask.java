package moe.zr.esmwiki.producer.task;

import lombok.extern.slf4j.Slf4j;
import moe.zr.enums.EventStatus;
import moe.zr.enums.EventType;
import moe.zr.esmwiki.producer.config.EventConfig;
import moe.zr.esmwiki.producer.repository.PointRankingRecordRepository;
import moe.zr.esmwiki.producer.repository.ScoreRankingRecordRepository;
import moe.zr.esmwiki.producer.service.impl.EventServiceImpl;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.pojo.*;
import moe.zr.service.DAQService;
import moe.zr.service.EventService;
import moe.zr.service.PointRankingService;
import moe.zr.service.SongRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@EnableScheduling
@Configuration
@Slf4j
public class RankingRecordTask {
    final static String cron = "0 */10 * * * ?";

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
    final
    MongoTemplate template;
    final
    EventService eventService;
    private final EventConfig eventConfig;
    final
    DAQService daqService;

    public RankingRecordTask(PointRankingService pointRankingService,
                             PointRankingRecordRepository pointRankingRecordRepository,
                             ScoreRankingRecordRepository scoreRankingRecordRepository,
                             SongRankingService songRankingService,
                             ReplyUtils replyUtils,
                             SimpleDateFormat simpleDateFormat,
                             MongoTemplate template,
                             EventService eventService,
                             EventConfig eventConfig, DAQService daqService) {
        this.pointRankingService = pointRankingService;
        this.pointRankingRecordRepository = pointRankingRecordRepository;
        this.scoreRankingRecordRepository = scoreRankingRecordRepository;
        this.songRankingService = songRankingService;
        this.replyUtils = replyUtils;
        this.simpleDateFormat = simpleDateFormat;
        this.template = template;
        this.eventService = eventService;
        this.eventConfig = eventConfig;
        this.daqService = daqService;
    }

    @Scheduled(cron = cron)
    private void task() {
        if (eventConfig.getIsOpen()) {
            batchGetPointRankingRecord();
            batchGetSongRankingRecord();
        }
    }

    @Scheduled(cron = "0 1 0/1 * * ?")
    private void refreshPointRanking() {
        if (eventConfig.getIsOpen()) {
            daqService.dropCollection();
            log.warn("成功删除集合");
            daqService.saveAllRanking();
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
        if (eventConfig.getIsOpen()) {
            String message = "今天的活动信息" +
                    "\n" + "\n" +
                    sendTodayEventPointRankingInfo() +
                    "\n" +
                    sendTodayEventPointRewardInfo() +
                    "\n" +
                    sendTodayEventSongRankingInfo();
            log.info(message);
            replyUtils.sendMessage(message);
            replyUtils.sendMessage(message, 1007284053);
        }
    }

    private String sendTodayEventPointRankingInfo() {
        return pointRankingService.onMessage("/pr now".split(" "));
    }

    private String sendTodayEventPointRewardInfo() {
        try {
            if (EventType.TOUR.equals(eventConfig.getEventType())) {
                return pointRankingService.batchGetTourEventPointRewardCount();
            } else {
                return pointRankingService.batchGetNormalEventPointRewardCount();
            }
        } catch (Exception e) {
            exceptionHandle(e);
            return "获取档位人数出错,可用的信息:" + e.getMessage();
        }
    }

    private String sendTodayEventSongRankingInfo() {
        String[] a = {"/sr"};
        return songRankingService.onMessage(a);
    }

    private void exceptionHandle(Exception e) {
        replyUtils.sendMessage("我罢工了");
        replyUtils.sendMessage(e.getMessage());
        eventConfig.setStatus(EventStatus.End);
    }

}
