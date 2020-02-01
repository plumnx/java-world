package com.plumnix.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class NacosDiscoveryClientCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosDiscoveryClientCommonApplication.class, args);
    }

    @Slf4j
    @RestController
    static class TestController {

        private final LoadBalancerClient loadBalancerClient;

        @Autowired
        public TestController(LoadBalancerClient loadBalancerClient) {
            this.loadBalancerClient = loadBalancerClient;
        }

        @GetMapping("/test")
        public String test() {
            ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-discovery-server");
            String url = serviceInstance.getUri() + "/hello?name=" + "plumnix";

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            return "Invoke: " + url + ", return: " + result;
        }

    }

}
