package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@SpringBootTest
class CardServiceImplTest {

    @Autowired
    MongoTemplate template;

    @Test
    void test() {
        Query query = new Query();
        query.fields().include("name");
        List<Card> cards = template.find(query, Card.class);
        System.out.println(cards);

    }
}