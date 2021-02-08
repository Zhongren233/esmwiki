package moe.zr.esmwiki.producer.repository;

import moe.zr.entry.Card;
import moe.zr.entry.hekk.Ranking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RankingRepository extends MongoRepository<Ranking, String> {

}
