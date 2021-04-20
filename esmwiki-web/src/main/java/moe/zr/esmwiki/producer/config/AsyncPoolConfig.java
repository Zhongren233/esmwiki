package moe.zr.esmwiki.producer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
@Configuration
public class AsyncPoolConfig implements AsyncConfigurer {
//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//        threadPoolTaskExecutor.setCorePoolSize(5);
//        threadPoolTaskExecutor.setMaxPoolSize(5);
//        threadPoolTaskExecutor.setMaxPoolSize(5);
//        threadPoolTaskExecutor.initialize();
//
//        return threadPoolTaskExecutor;
//    }
}
