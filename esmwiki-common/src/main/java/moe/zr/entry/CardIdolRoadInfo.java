package moe.zr.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CardIdolRoadInfo implements Serializable {
    private static final long serialVersionUID = -3369253425801351068L;
    private String sppSong;
    private String mvCostume;
    private String mvCostumeSCR;
    private String skillOrItemCouldGet;
}
