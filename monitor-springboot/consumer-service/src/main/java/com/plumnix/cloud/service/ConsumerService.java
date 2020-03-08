package com.plumnix.cloud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Service
public class ConsumerService {

    private final ProducerClient producerClient;

    @Autowired
    public ConsumerService(ProducerClient producerClient) {
        this.producerClient = producerClient;
    }


    @GetMapping("/name")
    public String callNameMethod(String name) {
        String result = producerClient.name("plumnix");
        log.info("ConsumerService result = " + result);
        return "return: " + result;
    }

    @GetMapping("/error")
    public String callErrorMethod(String error) {
        String result = producerClient.error("error");
        log.error("ConsumerService result = " + result);
        return "return: " + result;
    }

}
