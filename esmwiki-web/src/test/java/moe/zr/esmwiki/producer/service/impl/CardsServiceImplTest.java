package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.Card;
import moe.zr.service.CardsService;
import moe.zr.vo.Page;
import moe.zr.vo.WebVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CardsServiceImplTest {
    @Autowired
    CardsService cardsService;
    @Test
    void queryPageByCardVO() {
        WebVO<Card> cardVO = new WebVO<>();
        cardVO.setPage(1);
        cardVO.setSize(20);
        Page page = cardsService.queryPageByCardVO(cardVO);

    }
}