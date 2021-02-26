package moe.zr.esmwiki.producer.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
@Data
public class DAQConfig {
    @Value("${daq.token}")
    private String token;
    @Value("${daq.session}")
    private String session;

    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyyMMdd-HH:mm");
    }
}
