package moe.zr.entry.hekk;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

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
