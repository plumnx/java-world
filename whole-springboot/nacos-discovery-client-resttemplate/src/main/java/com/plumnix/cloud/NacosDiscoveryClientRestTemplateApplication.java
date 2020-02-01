package com.plumnix.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class NacosDiscoveryClientRestTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosDiscoveryClientRestTemplateApplication.class, args);
    }

    @Slf4j
    @RestController
    static class TestController {

        private final RestTemplate restTemplate;

        @Autowired
        public TestController(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @GetMapping("/test")
        public String test() {
            String result = restTemplate.getForObject("http://nacos-discovery-server/hello?name=plumnix", String.class);
            return "return: " + result;
        }

    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
