package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.ScoreRanking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRankingRepository extends MongoRepository<ScoreRanking, String> {
}
