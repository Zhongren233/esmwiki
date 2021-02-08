package moe.zr.esmwiki.producer.repository;

import moe.zr.entry.hekk.PointRanking;
import moe.zr.entry.hekk.ScoreRanking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRankingRepository extends MongoRepository<ScoreRanking, String> {
}
