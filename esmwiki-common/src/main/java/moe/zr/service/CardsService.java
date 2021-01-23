package moe.zr.service;

import moe.zr.entry.Card;
import moe.zr.vo.CardVO;
import org.springframework.data.domain.Page;

public interface CardsService {
    Page<Card> queryPageByCardVO(CardVO cardVO);


}
