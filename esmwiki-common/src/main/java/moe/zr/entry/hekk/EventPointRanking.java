package moe.zr.entry.hekk;

import lombok.Data;
import moe.zr.pojo.PointRanking;

import java.io.Serializable;
import java.util.List;

@Data
public class EventPointRanking implements Serializable {
    private static final long serialVersionUID = 5559780281018323108L;
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
