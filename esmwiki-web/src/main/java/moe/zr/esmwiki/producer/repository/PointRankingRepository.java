package moe.zr.esmwiki.producer.repository;

import lombok.extern.slf4j.Slf4j;
import moe.zr.pojo.PointRanking;
import moe.zr.pojo.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@EnableAsync
public interface PointRankingRepository extends MongoRepository<PointRanking, String> {
    List<PointRanking> findAllByUserProfile(UserProfile userProfile);

    Integer countByPointGreaterThanEqual(Integer point);

    @Async
    default void insertAsync(Iterable<PointRanking> iterable) {
        List<PointRanking> insert = insert(iterable);
    }
}
