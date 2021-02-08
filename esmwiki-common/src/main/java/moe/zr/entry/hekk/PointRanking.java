package moe.zr.entry.hekk;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("pointRanking")
public class PointRanking {
    @Id
    private String id;
    private UserProfile userProfile;
    private Integer userId;
    private Integer point;
    private Integer rank;
}
