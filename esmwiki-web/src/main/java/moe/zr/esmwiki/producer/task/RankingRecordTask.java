package moe.zr.esmwiki.producer.task;

import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.repository.PointRankingRecordRepository;
import moe.zr.esmwiki.producer.repository.ScoreRankingRecordRepository;
import moe.zr.pojo.PointRankingRecord;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

@EnableScheduling
@Configuration
@Slf4j
public class RankingRecordTask implements IMessageQuickReply {
    final
    PointRankingService pointRankingService;
    final
    PointRankingRecordRepository pointRankingRecordRepository;
    final
    ScoreRankingRecordRepository scoreRankingRecordRepository;
    final
    SongRankingService songRankingService;
    private boolean flag = true;


    final static String cron = "0 */1 * * * ?";

    public RankingRecordTask(PointRankingService pointRankingService, PointRankingRecordRepository pointRankingRecordRepository, ScoreRankingRecordRepository scoreRankingRecordRepository, SongRankingService songRankingService) {
        this.pointRankingService = pointRankingService;
        this.pointRankingRecordRepository = pointRankingRecordRepository;
        this.scoreRankingRecordRepository = scoreRankingRecordRepository;
        this.songRankingService = songRankingService;
    }

    @Scheduled(cron = cron)
    private void batchGetPointRankingRecord() {
        if (flag) {
            try {
                ArrayList<PointRankingRecord> pointRankingRecords = new ArrayList<>();
                pointRankingService.getRankingRecords().forEach(
                        a -> pointRankingRecords.add(new PointRankingRecord(a))
                );
                log.debug("成功获取");
            } catch (IOException | BadPaddingException | IllegalBlockSizeException | ParseException | ExecutionException | InterruptedException | RuntimeException e) {
                e.printStackTrace();
                flag = false;
            }
        }
    }

    @Scheduled(cron = cron)
    private void batchGetSongRankingRecord() {
        if (flag) {
            try {
                ArrayList<SongRankingRecord> songRankingRecords = new ArrayList<>();
                songRankingService.getSongRankingRecords().forEach(
                        a -> songRankingRecords.add(new SongRankingRecord(a))
                );
                log.debug("成功获取");
            } catch (IOException | BadPaddingException | IllegalBlockSizeException | ParseException | ExecutionException | InterruptedException | RuntimeException e) {
                e.printStackTrace();
                flag = false;
            }
        }
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
                        s += "\n即将运行:";
                        s += DateFormat.getDateTimeInstance().format(scheduled);
                    }
                    return s;
                case "set":
                    if (scheduled != null) {
                        return "已有任务:" + DateFormat.getDateTimeInstance().format(scheduled);
                    }
                    if (str.length == 2) {
                        return "没有参数";
                    }
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd-hhmm");
                        scheduled = simpleDateFormat.parse(str[2]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return "格式不正确 yyMMdd-hhmm";
                    }
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            scheduled = null;
                            flag = true;
                        }
                    }, scheduled);
                    return DateFormat.getDateTimeInstance().format(scheduled);
            }
        }
        return "/task {status} {set}";
    }

    @Override
    public String commandPrefix() {
        return "/task";
    }
}
