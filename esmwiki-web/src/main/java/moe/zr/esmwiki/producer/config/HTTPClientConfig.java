package moe.zr.esmwiki.producer.config;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HTTPClientConfig {
    @Bean
    public CloseableHttpAsyncClient closeableHttpClient()  {
        CloseableHttpAsyncClient aDefault = HttpAsyncClients.createSystem();
        aDefault.start();
        return aDefault;
    }

}
