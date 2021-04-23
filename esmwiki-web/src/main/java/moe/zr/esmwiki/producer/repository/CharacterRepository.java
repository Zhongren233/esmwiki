package moe.zr.esmwiki.producer.repository;

import moe.zr.entry.Character;
import moe.zr.pojo.BindUserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {
    List<Character> findByBirthMonth(String birthMonth);

    List<Character> findByBirthday(String birthDay);
}
