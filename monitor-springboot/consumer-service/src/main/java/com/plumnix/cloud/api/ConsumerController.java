package com.plumnix.cloud.api;

import com.plumnix.cloud.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ConsumerController {

    private final ConsumerService consumerService;

    @Autowired
    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping("/name")
    public String call() {
        String result = consumerService.callNameMethod("plumnix");
        log.info("ConsumerController.call result = " + result);
        return "return: " + result;
    }

    @GetMapping("/error")
    public String error() {
        String result = consumerService.callErrorMethod("error");
        log.error("ConsumerController.error result = " + result);
        return "return: " + result;
    }

}
