package com.plumnix.boot.service;

import com.plumnix.boot.batch.BatchLauncher;
import com.plumnix.boot.batch.SimpleJob;
import com.plumnix.boot.entity.BatchRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class BatchService {

    @Autowired
    private BatchLauncher batchLauncher;

    @Autowired
    private RecordService recordService;

    @Autowired
    private SimpleJob simpleJob;

    @Transactional
    public void run(BatchRecord oldBatchRecord) {
        if(oldBatchRecord == null ||oldBatchRecord.getId() == null) {
            log.warn("The task was null !");
        } else {
            BatchRecord batchRecord = recordService.selectByTaskId(oldBatchRecord.getId());
            if (null == batchRecord || batchRecord.getId() == null) {
                log.warn("Cann't find that task, task id: " + Objects.requireNonNull(batchRecord).getId());
            } else {
                try {
                    if (batchRecord.getStatus() != BatchRecord.RecordStatus.TODO.getStatus()) {
                        log.warn("Found that task, but status was not todo and task id: " + batchRecord.getId());
                    } else {
                        batchRecord.setStatus(BatchRecord.RecordStatus.DOING.getStatus());
                        // set ip
                        int num = recordService.update(batchRecord);
                        if (num == 1) {
                            Job job = simpleJob.build();
                            JobParameters jobParameters = new JobParametersBuilder().addDate("sys-date", new Date()).toJobParameters();
                            JobExecution jobExecution = batchLauncher.run(job, jobParameters, (execution, throwable) -> {
                                batchRecord.setJobInstanceId(execution.getJobId().intValue());
                                if (execution.getStatus() == BatchStatus.FAILED || null != throwable) {
                                    batchRecord.setVersion(batchRecord.getVersion() + 1);
                                    batchRecord.setStatus(BatchRecord.RecordStatus.FAIL.getStatus());
                                    recordService.update(batchRecord);
                                } else {
                                    batchRecord.setVersion(batchRecord.getVersion() + 1);
                                    batchRecord.setStatus(BatchRecord.RecordStatus.DONE.getStatus());
                                    recordService.update(batchRecord);
                                }
                            });
                            if (jobExecution.getStatus() == org.springframework.batch.core.BatchStatus.FAILED) {
                                batchRecord.setVersion(batchRecord.getVersion() + 1);
                                batchRecord.setStatus(BatchRecord.RecordStatus.FAIL.getStatus());
                                recordService.update(batchRecord);
                                log.error("The task status was wrong, task id:" + batchRecord.getId());
                            }
                        } else {
                            log.warn("The task status was wrong, task id:" + batchRecord.getId());
                        }
                    }
                } catch (Exception e) {
                    batchRecord.setStatus(BatchRecord.RecordStatus.FAIL.getStatus());
                    recordService.updateForce(batchRecord);
                    log.error("The task status was wrong, task id:" + batchRecord.getId(), e);
                }
            }
        }
    }

}
