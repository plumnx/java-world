package com.plumnix.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Configuration
public class ThreadPoolConfiguration {

//    @Bean
//    @Primary
//    public ExecutorService defaultThreadPool() {
//        new TransactionTemplate().execute(status -> {
//            status.flush();
//            return null;
//        });
//        return Executors.newFixedThreadPool(20);
//    }

}
