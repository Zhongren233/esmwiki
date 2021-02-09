package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.PointRankingRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRankingRecordRepository extends MongoRepository<PointRankingRecord, String> {
}
