package com.plumnix.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
public class NacosDiscoveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosDiscoveryServerApplication.class, args);
    }

    @Slf4j
    @RestController
    static class TestController {

        @GetMapping("/hello")
        public String hello(@RequestParam("name") String name) {
            log.info("invoked name = " + name);
            return "hello " + name;
        }

    }

}
