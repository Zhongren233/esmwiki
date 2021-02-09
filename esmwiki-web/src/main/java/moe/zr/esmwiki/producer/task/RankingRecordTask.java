package moe.zr.esmwiki.producer.task;

import moe.zr.esmwiki.producer.repository.PointRankingRecordRepository;
import moe.zr.esmwiki.producer.repository.ScoreRankingRecordRepository;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@EnableScheduling
@Configuration
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
                System.out.println(pointRankingRecordRepository.insert(pointRankingRecords));
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
                System.out.println(scoreRankingRecordRepository.insert(songRankingRecords));
            } catch (IOException | BadPaddingException | IllegalBlockSizeException | ParseException | ExecutionException | InterruptedException | RuntimeException e) {
                e.printStackTrace();
                flag = false;
            }
        }
    }


    @Override
    public String onMessage(String[] str) {
        System.out.println(flag);
        switch (str[1]) {
            case "status":
                return String.valueOf(flag);
            case "on":
                flag = true;
            case "off":
                flag = false;
            default:
                return String.valueOf(flag);
        }
    }

    @Override
    public String commandPrefix() {
        return "/task";
    }
}
