package com.plumnix.app.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.plumnix.app.mybatis.mapper")
@SpringBootApplication
public class MybatisPlusSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusSpringBootApplication.class, args);
    }

}
