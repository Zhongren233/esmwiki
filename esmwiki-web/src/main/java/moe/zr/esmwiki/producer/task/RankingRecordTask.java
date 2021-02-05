package moe.zr.esmwiki.producer.task;

import moe.zr.esmwiki.producer.repository.RankingRecordRepository;
import moe.zr.service.PointRankingService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

//@EnableScheduling
@Configuration
public class RankingRecordTask {
    final
    PointRankingService service;
    final
    RankingRecordRepository rankingRecordRepository;


    final static String cron = "0 */1 * * * ?";

    public RankingRecordTask(PointRankingService service, RankingRecordRepository rankingRecordRepository) {
        this.service = service;
        this.rankingRecordRepository = rankingRecordRepository;
    }

//    @Scheduled(cron = cron)
    private void batchGetRecord() {
        try {
            System.out.println(rankingRecordRepository.insert(service.getRankingRecords()));
        } catch (IOException | BadPaddingException | IllegalBlockSizeException | ParseException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
