package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.Car;
import moe.zr.pojo.Strategy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends MongoRepository<Car,String> {
    List<Car> findByTimeAfterAndTextContainsOrderByTimeDesc(Long time, String text);
}
