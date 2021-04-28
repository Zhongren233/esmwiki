package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.PointRanking;
import moe.zr.pojo.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PointRankingRepository extends MongoRepository<PointRanking, String> {
    List<PointRanking> findAllByUserProfile(UserProfile userProfile);

    Integer countByPointGreaterThanEqual(Integer point);
}
