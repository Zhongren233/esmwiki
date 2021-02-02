package moe.zr.esmwiki.producer.task;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.var;
import moe.zr.enums.EventRankingNavigationType;
import moe.zr.esmwiki.producer.repository.RankingRecordRepository;
import moe.zr.pojo.RankingRecord;
import moe.zr.service.PointRankingService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@EnableScheduling
@Configuration
public class RankingRecordTask {
    final
    PointRankingService service;
    final
    RankingRecordRepository rankingRecordRepository;
    final
    SimpleDateFormat dateFormat;

    final static String cron = "0 */1 * * * ?";

    public RankingRecordTask(PointRankingService service, RankingRecordRepository rankingRecordRepository, SimpleDateFormat dateFormat) {
        this.service = service;
        this.rankingRecordRepository = rankingRecordRepository;
        this.dateFormat = dateFormat;
    }


    private RankingRecord getRecord(JsonNode jsonNode) throws ParseException {
        String currentTime = jsonNode.get("current_time").textValue();
        Date date = dateFormat.parse(currentTime);
        JsonNode ranking = jsonNode.get("ranking");
        JsonNode x = ranking.get(0);
        int point = x.get("point").asInt();
        int rank = x.get("rank").asInt();
        int eventId = jsonNode.get("eventId").asInt();
        return new RankingRecord(null, date, eventId, point, rank);
    }

    @Scheduled(cron = cron)
    private void batchGetRecord() {
        var values = EventRankingNavigationType.values();
        ArrayList<RankingRecord> rankingRecords = new ArrayList<>();
        try {
            for (EventRankingNavigationType value : values) {
                RankingRecord record = getRecord(service.getRankingRecord(value));
                record.setRank(value.getRank());
                rankingRecords.add(record);
            }
            System.out.println(rankingRecordRepository.insert(rankingRecords));
        } catch (IOException | BadPaddingException | IllegalBlockSizeException | ParseException e) {
            e.printStackTrace();
        }
    }
}
