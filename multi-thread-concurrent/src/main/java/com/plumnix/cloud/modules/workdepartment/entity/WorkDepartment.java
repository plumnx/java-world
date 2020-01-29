package com.plumnix.cloud.modules.workdepartment.entity;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class WorkDepartment {

    private CountDownLatch countDownLatch;

    private List<WorkerPiece> workerPieces;

    private ExecutorService executorService;

    public WorkDepartment(ExecutorService executorService, List<WorkerPiece> workerPieces) {
        this.executorService = executorService;
        if(null != workerPieces) {
            countDownLatch = new CountDownLatch(workerPieces.size());
            this.workerPieces = workerPieces;
        }

        this.start();

    }

    public void start() {
        if(null != workerPieces) {
            this.workerPieces.forEach(workerPiece -> {
                this.executorService.execute(() -> {
                    workerPiece.accept(null);
                    this.countDownLatch.countDown();
                });
            });
        }
    }

}
