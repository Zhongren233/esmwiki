package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.ScoreRanking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableAsync
public interface ScoreRankingRepository extends MongoRepository<ScoreRanking, String> {
    @Async
    default void insertAsync(Iterable<ScoreRanking> iterable) {
        List<ScoreRanking> insert = insert(iterable);
    }
}
