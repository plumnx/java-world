package com.plumnix.cloud.api;

import com.plumnix.cloud.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProducerController {

    private final ProducerService producerService;

    @Autowired
    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/name")
    public String nameApi(@RequestParam("name") String name) {
        MDC.put("key_words", name);
        log.info("ProducerController.nameApi name = " + name);
        return producerService.nameMethod(name);
    }

    @GetMapping("/error")
    public String errorApi(@RequestParam("error") String error) {
        log.error("ProducerController.errorApi error = " + error);
        return producerService.errorMethod(error);
    }

}
