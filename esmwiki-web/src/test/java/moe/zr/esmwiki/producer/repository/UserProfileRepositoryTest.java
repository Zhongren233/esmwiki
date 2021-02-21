package moe.zr.esmwiki.producer.repository;

import moe.zr.entry.hekk.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

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

    @Test
    void antest() {
        Criteria formerNames = Criteria.where("formerNames").ne(null);
        Query query = new Query().addCriteria(formerNames);
        List<UserProfile> userProfiles = template.find(query, UserProfile.class);
        for (UserProfile userProfile : userProfiles) {
            List<String> formerNames1 = userProfile.getFormerNames();
            String s = formerNames1.get(0);
            userProfile.setName(s);
            repository.save(userProfile);
        }
    }
}