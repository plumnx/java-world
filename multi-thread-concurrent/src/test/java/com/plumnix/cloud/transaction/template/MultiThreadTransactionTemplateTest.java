package com.plumnix.cloud.transaction.template;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiThreadTransactionTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    private ExecutorService threadPool = Executors.newFixedThreadPool(20);

    @Test
    public void test_multi_thread_using_transaction_template_is_OK() throws InterruptedException {
        jdbcTemplate.execute("delete from test");
        jdbcTemplate.execute("insert into test values(0)");

        TransactionTemplate transactionTemplate =
                new TransactionTemplate(platformTransactionManager);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        IntStream.range(0, 10).forEach(value -> {
            threadPool.execute(() -> {
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            jdbcTemplate.execute("insert test values(" + value + ")");
                            if(value % 2 == 0) {
                                throw new Exception();
                            }
                        } catch(Exception e) {
                            status.setRollbackOnly();
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
                });
            });
        });

        jdbcTemplate.execute("insert into test values(11)");

        countDownLatch.await();
    }

}
