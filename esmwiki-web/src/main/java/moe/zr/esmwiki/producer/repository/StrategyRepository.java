package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.Strategy;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StrategyRepository extends MongoRepository<Strategy,String> {
    List<Strategy> findByHideFalse();

    Optional<Strategy> findByPrefix(String prefix);
}
