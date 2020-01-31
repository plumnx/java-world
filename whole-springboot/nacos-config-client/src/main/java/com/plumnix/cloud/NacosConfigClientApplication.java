package com.plumnix.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class NacosConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosConfigClientApplication.class, args);
    }

    @Slf4j
    @RestController
    @RefreshScope
    static class TestController {

        @Value("${test.title: Empty Title}")
        private String title;

        @GetMapping("/test")
        public String hello() {
            return title;
        }

    }

    @Slf4j
    @RestController
    @RefreshScope
    static class TestController2 {

        @Value("${test.title: Empty Title}")
        private String title;

        @Value("${test.aaa: Empty aaa}")
        private String aaa;

        @Value("${test.bbb: Empty bbb}")
        private String bbb;

        @Value("${test.profileLabel: Empty Profile Label}")
        private String profileLabel;

        @GetMapping("/test2")
        public String test2() {
            return title + ", " + aaa + ", " + bbb + ", " + profileLabel;
        }

    }

}
