package moe.zr.entry.hekk;

import lombok.Data;

import java.util.List;

@Data
public class EventSongRanking {
    private static final long serialVersionUID = 2956966050937498082L;
    private Integer eventId;
    private Integer pointRank;
    private Integer eventAssistMultiplyState;
    private Integer eventAssistMultiplyRemainTime;
    private Integer totalPages;
    private List<PointRanking> pointRanking;
    private Integer page;
    private String user;
    private Integer scoreRank;
}
