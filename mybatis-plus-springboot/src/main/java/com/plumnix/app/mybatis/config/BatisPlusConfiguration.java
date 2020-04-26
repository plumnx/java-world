package com.plumnix.app.mybatis.config;

import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatisPlusConfiguration {

    @Bean
    public IKeyGenerator iKeyGenerator() {
        return new H2KeyGenerator();
    }

}
