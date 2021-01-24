package moe.zr.service;

import moe.zr.entry.Card;
import moe.zr.vo.Page;
import moe.zr.vo.WebVO;

public interface CardsService {
    Page queryPageByCardVO(WebVO<Card> cardVO);


}
