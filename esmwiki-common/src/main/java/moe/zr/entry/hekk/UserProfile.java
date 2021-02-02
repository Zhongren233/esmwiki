package moe.zr.entry.hekk;

import lombok.Data;

@Data
public class UserProfile {
    private Integer id;
    private String name;
    private Award userAward1;
    private Award userAward2;
    private Integer favoriteCardId;
    private Boolean favoriteCardEvolved;
}
