package moe.zr.entry;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "card")
@NoArgsConstructor
@Accessors
public class Card implements Serializable {
    private static final long serialVersionUID = -7295866083951790960L;
    @Id
    private String id;
    private String name;
    private String character;
    private String appendDate;
    private Integer cardSortNum;
    private String implMethod;
    private String rarity;

    private String type;
    private String attribute;


    private CardValue cardValue;
    private CardImage cardImage;
    private CardSkill cardSkill;
    private CardIdolRoadInfo cardIdolRoadInfo;


}
