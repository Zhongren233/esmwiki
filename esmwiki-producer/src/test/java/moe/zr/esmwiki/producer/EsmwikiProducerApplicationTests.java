package moe.zr.esmwiki.producer;

import moe.zr.entry.Card;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;

@SpringBootTest
class EsmwikiProducerApplicationTests {
    @Autowired
    private MongoRepository<Card, ObjectId> repository;

    @Test
    void context() {

    }

    @Test
    void count() {
        System.out.println(repository.count());
    }
}
