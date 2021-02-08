package moe.zr.esmwiki.producer.repository;

import moe.zr.entry.hekk.PointRanking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RankingRepository extends MongoRepository<PointRanking, String> {

}
