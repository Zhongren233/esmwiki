package moe.zr.entry.hekk;

import java.util.List;

import lombok.Data;

@Data
public class EventSongRanking{
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
