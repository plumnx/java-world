package com.plumnix.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class NacosDiscoveryClientFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosDiscoveryClientFeignApplication.class, args);
    }

    @Slf4j
    @RestController
    static class TestController {

        private final Client client;

        @Autowired
        public TestController(Client client) {
            this.client = client;
        }

        @GetMapping("/test")
        public String test() {
            String result = client.hello("plumnix");
            return "return: " + result;
        }

    }

    @FeignClient("nacos-discovery-server")
    interface Client {

        @GetMapping("/hello")
        String hello(@RequestParam("name") String name);

    }

}
