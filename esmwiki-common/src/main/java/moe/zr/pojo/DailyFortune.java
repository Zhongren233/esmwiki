package moe.zr.pojo;

import lombok.Data;
import lombok.ToString;
import moe.zr.enums.Direction;
import moe.zr.enums.Luck;

import java.io.Serializable;
import java.util.List;

@Data
public class DailyFortune implements Serializable {
    private Luck luck;
    private Direction direction;
    private List<Fortune> luckyFortune;
    private List<Fortune> unluckyFortune;

}
