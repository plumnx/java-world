package com.plumnix.cloud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class ProducerService {

    public String nameMethod(String name) {
        log.info("ProducerService.nameMethod name = " + name);
        return "hello " + name + ", date: " + new Date();
    }

    public String errorMethod(String error) {
        log.error("ProducerService.errorMethod error = " + error);
        if(Objects.equals(error, "error")) {
            throw new RuntimeException("There was an error occurs!");
        }
        return "some " + error;
    }

}
