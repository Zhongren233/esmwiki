package moe.zr.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CardSkill implements Serializable {
    private static final long serialVersionUID = -1031063869620819565L;
    private String centerSkill;
    private String centerEffect;
    private String liveSkill;
    private String liveSkillInitEffect;
    private String liveSkillNoBreachMaxEffect;
    private String liveSkillMaxBreachMaxEffect;
    private String supportSkill;
    private String supportSkillInitEffect;
    private String supportSkillNoBreachMaxEffect;
    private String supportSkillMaxBreachMaxEffect;
}
