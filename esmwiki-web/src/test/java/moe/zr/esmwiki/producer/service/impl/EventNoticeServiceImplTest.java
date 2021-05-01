package moe.zr.esmwiki.producer.service.impl;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventNoticeServiceImplTest {
    @Test
    void test() {
        LocalDateTime of1 = LocalDateTime.of(2021, 5, 2, 21, 1);

        LocalDateTime of2 = LocalDateTime.of(2021, 5, 2, 22, 0);
        Duration between = Duration.between(of1, of2);
        System.out.println(between.toHours());
    }
}