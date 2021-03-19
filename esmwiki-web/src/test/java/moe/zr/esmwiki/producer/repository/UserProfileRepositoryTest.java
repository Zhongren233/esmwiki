package moe.zr.esmwiki.producer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import moe.zr.entry.hekk.UserProfile;

@SpringBootTest
class UserProfileRepositoryTest {
    @Autowired
    UserProfileRepository repository;
    @Autowired
    MongoTemplate template;

    @Test
    void test() {
        UserProfile byId = repository.findById(70000569);
        System.out.println(byId);
    }

}