package com.plumnix.app.mybatis.config;

import com.plumnix.app.mybatis.interceptor.*;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BatisConfiguration {

    private final List<Interceptor> interceptors;

    public BatisConfiguration() {
        interceptors = new ArrayList<>();

        interceptors.add(new UpdateExecutorInterceptor());
        interceptors.add(new CommitExecutorInterceptor());
        interceptors.add(new UpdateStatementHandlerInterceptor());
        interceptors.add(new ParameterHandlerInterceptor());
        interceptors.add(new PreparedStatementHandlerInterceptor());
    }

    @Bean
    ConfigurationCustomizer configurationCustomizer() {
        return configuration -> interceptors.forEach(configuration::addInterceptor);
    }

    @Bean
    com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer configurationCustomizerBatisPlus() {
        return configuration -> interceptors.forEach(configuration::addInterceptor);
    }

}
