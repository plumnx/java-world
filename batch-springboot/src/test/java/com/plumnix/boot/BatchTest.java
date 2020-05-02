package com.plumnix.boot;

import com.plumnix.boot.entity.BatchRecord;
import com.plumnix.boot.service.RecordService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.plumnix.boot.utils.Constants.BATCH_TOPIC;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatchTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RecordService recordService;

    @Test
    @Transactional
    public void send_task() {
        BatchRecord batchRecord = new BatchRecord();
        batchRecord.setId(uuid());
        batchRecord.setStatus(BatchRecord.RecordStatus.TODO.getStatus());
        batchRecord.setVersion(0);
        recordService.create(batchRecord);
        rocketMQTemplate.convertAndSend(BATCH_TOPIC, batchRecord);
    }

    private static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
