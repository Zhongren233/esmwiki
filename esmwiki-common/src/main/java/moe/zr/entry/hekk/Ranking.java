package moe.zr.entry.hekk;

import lombok.Data;

@Data
public class Ranking {
    private UserProfile userProfile;
    private Integer userId;
    private Integer point;
    private Integer rank;
}
