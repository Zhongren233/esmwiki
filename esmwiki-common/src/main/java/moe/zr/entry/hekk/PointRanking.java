package moe.zr.entry.hekk;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("pointRanking")
public class PointRanking implements Serializable {
    private static final long serialVersionUID = -8312313590019228395L;
    @Id
    private String id;
    private Integer eventId;
    private Integer userId;
    private Integer point;
    private Integer rank;
    private UserProfile userProfile;
}
