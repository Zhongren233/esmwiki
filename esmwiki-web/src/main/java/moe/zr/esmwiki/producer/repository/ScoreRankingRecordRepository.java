package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.SongRankingRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRankingRecordRepository extends MongoRepository<SongRankingRecord, String> {
}
