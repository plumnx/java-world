package com.plumnix.boot.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.function.BiConsumer;

@Component
public class BatchLauncher implements InitializingBean {

    protected static final Log logger = LogFactory.getLog(SimpleJobLauncher.class);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TaskExecutor taskExecutorPool;

    /**
     * Run the provided job with the given {@link JobParameters}. The
     * {@link JobParameters} will be used to determine if this is an execution
     * of an existing job instance, or if a new one should be created.
     *
     * @param job           the job to be run.
     * @param jobParameters the {@link JobParameters} for this particular
     *                      execution.
     * @return the {@link JobExecution} if it returns synchronously. If the
     * implementation is asynchronous, the status might well be unknown.
     * @throws JobExecutionAlreadyRunningException if the JobInstance already
     *                                             exists and has an execution already running.
     * @throws JobRestartException                 if the execution would be a re-start, but a
     *                                             re-start is either not allowed or not needed.
     * @throws JobInstanceAlreadyCompleteException if this instance has already
     *                                             completed successfully
     * @throws JobParametersInvalidException       thrown if jobParameters is invalid.
     */
    public JobExecution run(final Job job, final JobParameters jobParameters, BiConsumer<JobExecution, Throwable> consumer)
            throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {

        Assert.notNull(job, "The Job must not be null.");
        Assert.notNull(jobParameters, "The JobParameters must not be null.");

        // Check the validity of the parameters before doing creating anything
        // in the repository...
        job.getJobParametersValidator().validate(jobParameters);

        /*
         * There is a very small probability that a non-restartable job can be
         * restarted, but only if another process or thread manages to launch
         * <i>and</i> fail a job execution for this instance between the last
         * assertion and the next method returning successfully.
         */
        final JobExecution jobExecution = jobRepository.createJobExecution(job.getName(), jobParameters);

        try {
            taskExecutorPool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        logger.info("Job: [" + job + "] launched with the following parameters: [" + jobParameters
                                + "]");
                        job.execute(jobExecution);
                        logger.info("Job: [" + job + "] completed with the following parameters: [" + jobParameters
                                + "] and the following status: [" + jobExecution.getStatus() + "]");
                        consumer.accept(jobExecution, null);
                    } catch (Throwable t) {
                        consumer.accept(jobExecution, t);
                        logger.info("Job: [" + job
                                + "] failed unexpectedly and fatally with the following parameters: [" + jobParameters
                                + "]", t);
                        rethrow(t);
                    }
                }

                private void rethrow(Throwable t) {
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException) t;
                    } else if (t instanceof Error) {
                        throw (Error) t;
                    }
                    throw new IllegalStateException(t);
                }
            });
        } catch (TaskRejectedException e) {
            jobExecution.upgradeStatus(BatchStatus.FAILED);
            if (jobExecution.getExitStatus().equals(ExitStatus.UNKNOWN)) {
                jobExecution.setExitStatus(ExitStatus.FAILED.addExitDescription(e));
            }
            jobRepository.update(jobExecution);
        }

        return jobExecution;
    }

    /**
     * Set the JobRepository.
     *
     * @param jobRepository instance of {@link JobRepository}.
     */
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Set the TaskExecutor. (Optional)
     *
     * @param taskExecutorPool instance of {@link TaskExecutor}.
     */
    public void setTaskExecutorPool(TaskExecutor taskExecutorPool) {
        this.taskExecutorPool = taskExecutorPool;
    }

    /**
     * Ensure the required dependencies of a {@link JobRepository} have been
     * set.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(jobRepository != null, "A JobRepository has not been set.");
        if (taskExecutorPool == null) {
            logger.info("No TaskExecutor has been set, defaulting to synchronous executor.");
            taskExecutorPool = new SyncTaskExecutor();
        }
    }

}
