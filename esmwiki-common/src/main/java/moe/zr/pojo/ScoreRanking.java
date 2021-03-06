package moe.zr.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("scoreRanking")
public class ScoreRanking {
    @Id
    private String id;
    private Integer eventId;
    private Integer userId;
    private Integer point;
    private Integer rank;
}
