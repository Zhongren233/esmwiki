package moe.zr.esmwiki.producer.config;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 我受不了了 我决定开两个client
 * 一个用于爬榜，一个用于处理qq消息
 */
@Configuration
public class HTTPClientConfig {
    @Bean
    public CloseableHttpAsyncClient normalClient() {
        CloseableHttpAsyncClient aDefault = HttpAsyncClientBuilder.create()
                .useSystemProperties()
                .build();
        aDefault.start();
        return aDefault;
    }

    @Bean
    public CloseableHttpAsyncClient eventClient() {
        CloseableHttpAsyncClient aDefault = HttpAsyncClientBuilder.create()
                .useSystemProperties()
                .build();
        aDefault.start();
        return aDefault;
    }

}
