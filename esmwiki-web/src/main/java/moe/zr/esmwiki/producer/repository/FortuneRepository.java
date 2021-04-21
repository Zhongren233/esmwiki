package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.Fortune;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FortuneRepository extends MongoRepository<Fortune, String> {
    @Aggregation(pipeline = "{$sample: {size: 1}}")
    Fortune getOneRandom();

    @Aggregation(pipeline = "{$sample: {size: ?0}}")
    List<Fortune> getRandom(int size);

}
