package moe.zr.esmwiki.producer.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DailyFortuneProducerTest {
    @Autowired
    DailyFortuneProducer dailyFortuneProducer;
    @Test
    void getDailyFortune() {
        for (int i = 0; i < 10; i++) {
            System.out.println(dailyFortuneProducer.getDailyFortune());
        }
    }
}