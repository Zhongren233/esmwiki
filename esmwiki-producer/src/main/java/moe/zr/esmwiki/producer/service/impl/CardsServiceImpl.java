package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.Card;
import moe.zr.esmwiki.producer.util.CardVOUtils;
import moe.zr.service.CardsService;
import moe.zr.vo.CardVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@DubboService
@Path("/api/cards")
public class CardsServiceImpl implements CardsService {
    final MongoRepository<Card, String> repository;

    public CardsServiceImpl(MongoRepository<Card, String> repository) {
        this.repository = repository;
    }

    /**
     * fastjson就是个弱智玩意 不要用
     */
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Page<Card> queryPageByCardVO(CardVO cardVO) {
        PageRequest pageRequest = CardVOUtils.parsePageRequest(cardVO);
        if (cardVO.getCard() != null) {
            Example<Card> of = Example.of(cardVO.getCard());
            return repository.findAll(of, pageRequest);
        } else
            return repository.findAll(pageRequest);
    }

}
