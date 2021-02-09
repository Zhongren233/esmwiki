package moe.zr.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingRecord {
    private String currentTime;
    private Integer eventId;
    private Integer point;
    private Integer rank;
}
