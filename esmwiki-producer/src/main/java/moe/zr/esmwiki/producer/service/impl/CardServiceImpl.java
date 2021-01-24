package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.Card;
import moe.zr.service.CardService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@DubboService(interfaceClass = CardService.class)
@Path("/api/card")
public class CardServiceImpl implements CardService {
    final MongoRepository<Card, String> repository;

    public CardServiceImpl(MongoRepository<Card, String> repository) {
        this.repository = repository;
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Card save(Card card) {
        return repository.save(card);
    }

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Card query(Card card) {
        String id = card.getId();
        if (id == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
        Optional<Card> byId = repository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
