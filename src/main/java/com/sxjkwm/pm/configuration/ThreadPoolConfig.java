package com.sxjkwm.pm.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer {
    private static final int coreThreadSize = Runtime.getRuntime().availableProcessors() * 2;
    private static final int maxThreadSize = coreThreadSize;
    private static final long keepAlive = 3000;
    private static final TimeUnit keepAliveTimeUnit = TimeUnit.MILLISECONDS;
    private static final int queueSize = 2048;
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(queueSize);

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreThreadSize, maxThreadSize,
                keepAlive, keepAliveTimeUnit, queue, Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        return AsyncConfigurer.super.getAsyncExecutor();
    }

}
