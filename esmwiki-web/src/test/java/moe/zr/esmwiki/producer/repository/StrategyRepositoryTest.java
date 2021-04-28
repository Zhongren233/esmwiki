package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.Strategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
@SpringBootTest
class StrategyRepositoryTest {
    @Autowired
    StrategyRepository strategyRepository;
    @Test
    void name() {
        strategyRepository.findByHideFalse().forEach(System.out::println);
    }

    @Test
    void name1() {
        Optional<Strategy> optionalStrategy = strategyRepository.findByPrefix("测试");
        if (optionalStrategy.isPresent()) {
            System.out.println(optionalStrategy);
        }else {
            System.out.println("null");
        }

        optionalStrategy = strategyRepository.findByPrefix("aaa");
        if (optionalStrategy.isPresent()) {
            System.out.println(optionalStrategy);
        }else {
            System.out.println("null");
        }

    }
}