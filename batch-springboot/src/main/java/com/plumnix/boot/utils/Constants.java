package com.plumnix.boot.utils;

import java.util.UUID;

public class Constants {

    public static final String BATCH_TOPIC = "BatchTopic";

    public static final String BATCH_CONSUMER = "batchConsumer";

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
