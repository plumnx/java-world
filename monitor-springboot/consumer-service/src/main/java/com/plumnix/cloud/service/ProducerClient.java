package com.plumnix.cloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("producer-service")
public interface ProducerClient {

    @GetMapping("/name")
    String name(@RequestParam("name") String name);

    @GetMapping("/error")
    String error(@RequestParam("error") String error);

}
