package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.RankingRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRecordRepository extends MongoRepository<RankingRecord, String> {
}
