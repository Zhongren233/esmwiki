package moe.zr.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CardValue implements Serializable {
    private static final long serialVersionUID = 3630724711094837053L;
    private Integer initCompositeValue;
    private Integer initDaValue;
    private Integer initVoValue;
    private Integer initPfValue;
    private Integer noBreachCompositeValue;
    private Integer noBreachDaValue;
    private Integer noBreachVoValue;
    private Integer noBreachPfValue;
    private Integer allBreachCompositeValue;
    private Integer allBreachDaValue;
    private Integer allBreachVoValue;
    private Integer allBreachPfValue;
}
