package com.plumnix.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@EnableDiscoveryClient
@SpringBootApplication
public class NacosDiscoveryClientWebclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosDiscoveryClientWebclientApplication.class, args);
    }

    @RestController
    static class TestController {

        @Autowired
        private WebClient.Builder webClientBuilder;

        @GetMapping("/test")
        public Mono<String> test() {
            return webClientBuilder.build().
                    get().
                    uri("http://nacos-discovery-server/hello?name=plumnix").
                    retrieve().
                    bodyToMono(String.class);
        }

    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

}
