package moe.zr.esmwiki.producer.repository;

import moe.zr.pojo.Fortune;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FortuneRepositoryTest {
    @Autowired
    FortuneRepository fortuneRepository;

    @Test
    void getOneRandom() {
        Fortune oneRandom = fortuneRepository.getOneRandom();
        System.out.println(oneRandom);
    }

    @Test
    void insertOne() {
        Fortune fortune = new Fortune();
        fortune.setName("111");
        fortune.setSuitable("222");
        fortune.setUnsuitable("333");
        System.out.println(fortuneRepository.insert(fortune));
    }

    @Test
    void getOne() {
        Optional<Fortune> byId = fortuneRepository.findById("60800db4df7af30bb056bb88");
    }

    @Test
    void getRandom() {
        System.out.println(fortuneRepository.getRandom(3));
    }
}