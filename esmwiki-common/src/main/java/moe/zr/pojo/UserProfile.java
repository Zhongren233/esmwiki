package moe.zr.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Document("userProfile")
@Accessors(chain = true)
public class UserProfile {
    @Id
    @JsonIgnore
    private String _id;
    private Integer id;
    private String name;
    private Award userAward1;
    private Award userAward2;
    private Integer favoriteCardId;
    private Boolean favoriteCardEvolved;
    private Integer eventId;
}
