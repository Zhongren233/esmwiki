package moe.zr.esmwiki.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(proxyBeanMethods = false)
@EnableConfigurationProperties
public class EsmwikiProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsmwikiProducerApplication.class, args);
    }

}
