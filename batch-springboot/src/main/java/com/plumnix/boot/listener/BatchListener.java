package com.plumnix.boot.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plumnix.boot.entity.BatchRecord;
import com.plumnix.boot.service.BatchService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.plumnix.boot.utils.Constants.BATCH_CONSUMER;
import static com.plumnix.boot.utils.Constants.BATCH_TOPIC;

@Slf4j
@Service
@RocketMQMessageListener(topic = BATCH_TOPIC, consumerGroup = BATCH_CONSUMER)
public class BatchListener implements RocketMQListener<BatchRecord> {

    @Autowired
    private BatchService batchService;

    @SneakyThrows
    @Override
    public void onMessage(BatchRecord batchRecord) {
        log.info(new ObjectMapper().writeValueAsString(batchRecord));
        batchService.run(batchRecord);
    }

}
