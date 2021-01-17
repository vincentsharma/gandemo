package com.gan.gandemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class Config {

    @Value("${ovenCount}")
    private Integer ovenCount;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(ovenCount);
        pool.setMaxPoolSize(ovenCount);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }
}
