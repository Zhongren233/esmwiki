package moe.zr.esmwiki.producer.service.impl;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class EventNoticeServiceImplTest {
    @Test
    void test() {
        LocalDateTime of1 = LocalDateTime.of(2021, 5, 1, 10, 1);
        LocalDateTime of2 = LocalDateTime.of(2021, 5, 2, 22, 0);
        Duration between = Duration.between(of1, of2);
        System.out.println("between.toHoursPart() = " + between.toHoursPart());
        System.out.println("between.toHours() = " + between.toHours());
    }
}