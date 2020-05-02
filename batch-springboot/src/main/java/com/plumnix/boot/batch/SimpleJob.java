package com.plumnix.boot.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SimpleJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private TaskExecutor taskExecutorPool;

    public Job build() {
        ItemReader itemReader = new SimpleItemReader();
        ItemProcessor itemProcessor = new SimpleItemProcessor();
        ItemWriter itemWriter = new SimpleItemWriter();

        Step step = stepBuilderFactory.
                get(UUID.randomUUID().toString()).
                chunk(Integer.MAX_VALUE).
                reader(itemReader).
                processor(itemProcessor).
                writer(itemWriter).
                taskExecutor(taskExecutorPool).
                throttleLimit(10).
                build();

        return jobBuilderFactory.
                get(UUID.randomUUID().toString()).
                start(step).
                build();
    }

    public static class SimpleItemReader implements ItemReader<Integer>{

        private Iterator<Integer> iterator;

        public SimpleItemReader() {
            List<Integer> list = IntStream.range(0, 10).boxed().collect(Collectors.toList());
            this.iterator = list.iterator();
        }

        @Override
        public synchronized Integer read() {
            if(this.iterator.hasNext()) {
                return this.iterator.next();
            }
            return null;
        }
    }

    public static class SimpleItemProcessor implements ItemProcessor<Integer, String> {
        @Override
        public String process(Integer item) {
            return item.toString();
        }
    }

    @Slf4j
    public static class SimpleItemWriter implements ItemWriter<String> {
        @Override
        public void write(List<? extends String> items) throws Exception {
            log.info("result = {}", new ObjectMapper().writeValueAsString(items));
        }
    }

}
