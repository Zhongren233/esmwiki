package moe.zr.esmwiki.producer.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DAQConfigTest {
    @Autowired
    DAQConfig config;
    @Test

    void getToken() {
        System.out.println(config.getToken());
    }

    @Test
    void getSession() {
        System.out.println(config.getSession());
    }
}