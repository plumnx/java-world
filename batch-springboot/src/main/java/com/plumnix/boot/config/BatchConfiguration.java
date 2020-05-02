package com.plumnix.boot.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    /*
     * 作业仓库,把job运行过程中产生的数据持久化到数据库
     * HikariDataSource 数据源,多个数据源的时候指定按名称注入
     */
    @Bean
    @Override
    public JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(platformTransactionManager);
        jobRepositoryFactoryBean.setDatabaseType(DatabaseType.POSTGRES.name());
        jobRepositoryFactoryBean.setValidateTransactionState(false);
        return jobRepositoryFactoryBean.getObject();
    }

    /**
     * JobBuilderFactory
     *
     * @param jobRepository JobRepository
     * @return JobBuilderFactory
     */
//    @Bean
//    JobBuilderFactory jobBuilderFactory(JobRepository jobRepository) {
//        return new JobBuilderFactory(jobRepository);
//    }

    /**
     * StepBuilderFactory
     *
     * @param jobRepository                jobRepository
     * @param dataSourceTransactionManager dataSourceTransactionManager
     * @return stepBuilderFactory
     */
//    @Bean
//    StepBuilderFactory stepBuilderFactory(JobRepository jobRepository, DataSourceTransactionManager dataSourceTransactionManager) {
//        return new StepBuilderFactory(jobRepository, dataSourceTransactionManager);
//    }

    /**
     * 作业调度器
     */
//    @Bean
//    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
//        BatchLauncher jobLauncher = new BatchLauncher();
//        jobLauncher.setJobRepository(jobRepository);
//        jobLauncher.setTaskExecutor(taskExecutor());
//        return jobLauncher;
//    }

    /**
     * 作业注册器
     */
    @Bean
    public MapJobRegistry mapJobRegistry() {
        return new MapJobRegistry();
    }


    /*** JobRegistryBeanPostProcessor
     *
     * @param mapJobRegistry MapJobRegistry
     * @return JobRegistryBeanPostProcessor
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(MapJobRegistry mapJobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(mapJobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    /**
     * 作业线程池
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutorPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setKeepAliveSeconds(30000);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(1024);
        return threadPoolTaskExecutor;
    }
}
