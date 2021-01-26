package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.Card;
import moe.zr.esmwiki.producer.util.WebVOUtils;
import moe.zr.service.CardsService;
import moe.zr.vo.Page;
import moe.zr.vo.WebVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@DubboService
@Component
@Path("/api/cards")
public class CardsServiceImpl implements CardsService {

    final
    MongoTemplate template;

    public CardsServiceImpl(MongoTemplate template) {
        this.template = template;
    }


    /**
     * fastjson就是个弱智玩意 不要用
     * 操你妈 凭什么get不能带body
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Page queryPageByCardVO(WebVO<Card> cardVO) {
        PageRequest pageRequest = WebVOUtils.parsePageRequest(cardVO, Card.class);
        Card data = cardVO.getData();
        Query query = new Query();
        query.fields().include("name",
                "id",
                "character",
                "cardValue",
                "cardImage",
                "type",
                "rarity",
                "appendDate",
                "attribute");
        if (data != null) {
            Criteria criteria = Criteria.byExample(Example.of(data));
            query.addCriteria(criteria);
        }
        long count = template.count(query, Card.class);
        query.with(pageRequest);
        List<Card> cards = template.find(query, Card.class);
        return WebVOUtils.buildPage(cards, count, cardVO);
    }

}
