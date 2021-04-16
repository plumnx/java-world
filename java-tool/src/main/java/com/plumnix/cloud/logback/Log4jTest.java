package com.plumnix.cloud.logback;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.time.LocalDateTime;

public class Log4jTest {

    public static void main(String[] args) throws InterruptedException {
        FileAppender appender = new FileAppender();
        appender.setFile("./A.log");
        appender.setLayout(new PatternLayout("%d [%t] %-5p %c - %m%n"));
        appender.activateOptions();

        new Thread(new Writer(appender,"com.company.A")).start();
        new Thread(new Writer(appender,"com.company.B")).start();
    }

    public static class Writer implements Runnable {

        private FileAppender fileAppender;
        private String name;

        public Writer(FileAppender fileAppender, String name) {
            this.fileAppender = fileAppender;
            this.name = name;
        }

        @Override
        public void run() {
            // Get logger and add appender
            Logger logger = Logger.getLogger(name);
            logger.setAdditivity(false);
            logger.addAppender(this.fileAppender);

            // Task
            LocalDateTime before = LocalDateTime.now();
            while(true) {
                logger.info("Hello World!");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(LocalDateTime.now().minusSeconds(before.getSecond()).getSecond() > 3) {
                    break;
                }
            }

            // Remove appender
            logger.removeAppender(this.fileAppender);
        }

    }

}
