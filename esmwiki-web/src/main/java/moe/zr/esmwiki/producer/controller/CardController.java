package moe.zr.esmwiki.producer.controller;

import moe.zr.service.CardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/card")
public class CardController {
    final
    CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


}
