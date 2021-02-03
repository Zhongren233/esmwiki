package moe.zr.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "rankingRecord")
public class RankingRecord {
    @Id
    private String id;
    private String currentTime;
    private Integer eventId;
    private Integer point;
    private Integer rank;
}
