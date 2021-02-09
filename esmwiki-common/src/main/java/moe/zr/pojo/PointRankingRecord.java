package moe.zr.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "pointRankingRecord")
@Getter
@Setter
public class PointRankingRecord extends RankingRecord {
    @Id
    private String id;

    public PointRankingRecord(String currentTime, Integer eventId, Integer point, Integer rank) {
        super(currentTime, eventId, point, rank);
    }

    public PointRankingRecord(RankingRecord rankingRecord) {
        this(rankingRecord.getCurrentTime(), rankingRecord.getEventId(), rankingRecord.getPoint(), rankingRecord.getRank());
    }
}
