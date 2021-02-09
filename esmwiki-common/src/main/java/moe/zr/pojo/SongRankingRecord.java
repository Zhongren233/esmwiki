package moe.zr.pojo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "songRankingRecord")
@Getter
@Setter
public class SongRankingRecord extends RankingRecord {
    @Id
    private String id;

    public SongRankingRecord(String currentTime, Integer eventId, Integer point, Integer rank) {
        super(currentTime, eventId, point, rank);
    }

    public SongRankingRecord(RankingRecord rankingRecord) {
        this(rankingRecord.getCurrentTime(), rankingRecord.getEventId(), rankingRecord.getPoint(), rankingRecord.getRank());
    }
}
