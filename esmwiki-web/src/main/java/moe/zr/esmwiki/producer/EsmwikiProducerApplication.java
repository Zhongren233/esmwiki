package moe.zr.esmwiki.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class EsmwikiProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsmwikiProducerApplication.class, args);
    }

}
