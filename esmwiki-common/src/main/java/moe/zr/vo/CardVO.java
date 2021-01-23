package moe.zr.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import moe.zr.entry.Card;

/**
 * 弱智fastjson让我专门写个vo对象 我直接辱骂
 */
@Data
@Accessors
@NoArgsConstructor
public class CardVO {
    Integer page;
    Integer size;
    String sort;
    Card card;
}
