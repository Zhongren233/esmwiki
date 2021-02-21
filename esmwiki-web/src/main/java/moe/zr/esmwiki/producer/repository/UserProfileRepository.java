package moe.zr.esmwiki.producer.repository;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import moe.zr.entry.hekk.UserProfile;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    UserProfile findById(Integer id);

    default UserProfile updateByUserId(UserProfile profile) {
        Integer id = profile.getId();
        UserProfile byId = findById(id);
        if (byId!=null) {
            String _id = byId.get_id();
            List<String> formerNames = byId.getFormerNames();
            if (formerNames == null) {
                formerNames = new ArrayList<>();
            }
            formerNames.add(byId.getName());
            profile.set_id(_id);
            profile.setFormerNames(formerNames);
        }
        System.out.println(profile);
        return save(profile);
    }

}
