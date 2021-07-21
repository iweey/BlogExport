package com.blogexport.common;

import java.util.concurrent.LinkedBlockingQueue;

public class SingletonQueue {

    private static volatile LinkedBlockingQueue INSTANCE;

    private SingletonQueue() {

    }

    public static LinkedBlockingQueue getInstance() {
        if (INSTANCE == null) {
            synchronized (SingletonQueue.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LinkedBlockingQueue();
                }
            }
        }
        return INSTANCE;
    }
}