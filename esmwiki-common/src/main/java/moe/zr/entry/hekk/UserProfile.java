package moe.zr.entry.hekk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
}
