package com.plumnix.boot.api;

import com.plumnix.boot.entity.BatchRecord;
import com.plumnix.boot.service.RecordService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

import static com.plumnix.boot.utils.Constants.BATCH_TOPIC;
import static com.plumnix.boot.utils.Constants.uuid;

@RestController
public class BatchController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("sendMessage")
    public void sendMessage() {
        BatchRecord batchRecord = new BatchRecord();
        batchRecord.setId(uuid());
        batchRecord.setStatus(BatchRecord.RecordStatus.TODO.getStatus());
        batchRecord.setVersion(0);

        recordService.create(batchRecord);
        IntStream.range(0, 10).forEach(no -> {
            rocketMQTemplate.convertAndSend(BATCH_TOPIC, batchRecord);
        });
    }

}
