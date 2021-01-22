package moe.zr.service;


import moe.zr.entry.Card;
import moe.zr.vo.CardVO;
import org.springframework.data.domain.Page;


public interface CardService {
    Page<Card> queryPageByCardVO(CardVO cardVO);

    Card insertCard(Card card);

    Long count();
}