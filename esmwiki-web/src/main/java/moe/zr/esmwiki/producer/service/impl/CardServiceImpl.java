package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.Card;
import moe.zr.service.CardService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    final MongoRepository<Card, String> repository;

    public CardServiceImpl(MongoRepository<Card, String> repository) {
        this.repository = repository;
    }


    public Card save(Card card) {
        return repository.save(card);
    }


    public Card query(Card card) {
        String id = card.getId();
        if (id == null) throw new IllegalArgumentException();
        Optional<Card> byId = repository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else throw new NoSuchElementException();

    }
}
