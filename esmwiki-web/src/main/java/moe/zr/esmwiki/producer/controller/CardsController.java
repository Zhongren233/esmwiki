package moe.zr.esmwiki.producer.controller;

import moe.zr.entry.Card;
import moe.zr.service.CardsService;
import moe.zr.vo.Page;
import moe.zr.vo.WebVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
public class CardsController {
    final
    CardsService service;


    public CardsController(CardsService cardService) {
        this.service = cardService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Page getCards(@RequestBody WebVO<Card> cardVo){
        System.out.println(cardVo);
        return service.queryPageByCardVO(cardVo);
    }
}
