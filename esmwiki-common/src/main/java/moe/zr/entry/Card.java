package moe.zr.entry;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.var;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashMap;

@Data
@Document(collection = "card")
@NoArgsConstructor
@Accessors
public class Card implements Serializable {
    private static final long serialVersionUID = -7295866083951790960L;
    @Id
    private String id;
    private String cardName;
    private String appendDate;
    private Integer cardSortNum;
    private String implMethod;
    private String rarity;
    private Integer noBreachMaxLv;
    private String type;
    private String attribute;
    private String characterNamePhoneticNotation;
    private Integer noBreachMaxFans;

    private CardValue cardValue;
    private CardImage cardImage;
    private CardSkill cardSkill;
    private CardIdolRoadInfo cardIdolRoadInfo;


}
