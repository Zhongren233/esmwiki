package moe.zr.esmwiki.producer.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HTTPClientConfig {
    @Bean
    public CloseableHttpClient closeableHttpClient() {
        return HttpClientBuilder.create().build();
    }

}
