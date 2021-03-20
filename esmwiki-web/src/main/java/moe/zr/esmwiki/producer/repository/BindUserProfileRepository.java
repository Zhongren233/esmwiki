package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.BindUserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BindUserProfileRepository extends MongoRepository<BindUserProfile, String> {
    BindUserProfile findBindUserProfileByQqNumber(Long qqNumber);
}
