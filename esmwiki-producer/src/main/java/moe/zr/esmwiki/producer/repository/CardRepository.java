package moe.zr.esmwiki.producer.repository;

import moe.zr.entry.Card;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CardRepository extends MongoRepository<Card, String> {

}
