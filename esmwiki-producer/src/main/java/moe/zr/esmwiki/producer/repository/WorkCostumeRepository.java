package moe.zr.esmwiki.producer.repository;

import moe.zr.entry.WorkCostume;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkCostumeRepository extends MongoRepository<WorkCostume, String> {

}
