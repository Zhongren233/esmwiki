package moe.zr.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
public class RankingRecord {
    private Long currentTime;
    private Integer eventId;
    private Integer point;
    private Integer rank;
}
