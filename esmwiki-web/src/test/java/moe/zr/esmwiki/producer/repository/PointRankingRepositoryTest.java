package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.PointRanking;
import moe.zr.pojo.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PointRankingRepositoryTest {
    @Autowired
    PointRankingRepository pointRankingRepository;

    @Test
    void test() {
        PointRanking pointRanking = new PointRanking();
        UserProfile userProfile = new UserProfile();
        userProfile.setName("雪泉");
        pointRanking.setUserProfile(userProfile);

        System.out.println(pointRankingRepository.findAllByUserProfile(userProfile));
    }
}