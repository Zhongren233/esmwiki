package moe.zr.esmwiki.producer.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class DAQConfig {
    @Value("${daq.token}")
    private String token;
    @Value("${daq.session}")
    private String session;

}
