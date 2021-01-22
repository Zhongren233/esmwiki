package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.Card;
import moe.zr.vo.CardVO;
import moe.zr.service.CardService;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@DubboService(interfaceClass = CardService.class)
@Path("card")
public class CardServiceImpl implements CardService {
    final MongoRepository<Card, ObjectId> repository;

    public CardServiceImpl(MongoRepository<Card, ObjectId> repository) {
        this.repository = repository;
    }

    @Override
    @Path("/count")
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    public Long count() {
        return repository.count();
    }

    /**
     * fastjson就是个弱智玩意 不要用
     */
    @Path("/query")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Page<Card> queryPageByCardVO(CardVO cardVO) {
        PageRequest pageRequest = CardVOUtils.parsePageRequest(cardVO);
        Example<Card> of = Example.of(cardVO.getCard());
        try {
            return repository.findAll(of, pageRequest);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage());
        }
    }


    @Override
    @Path("/insert")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Card insertCard(Card card) {
        return repository.insert(card);
    }
}
