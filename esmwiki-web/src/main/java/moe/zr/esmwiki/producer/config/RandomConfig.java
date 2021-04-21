package moe.zr.esmwiki.producer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class RandomConfig {
    @Bean
    public Random random() {
        return new Random();
    }
}
