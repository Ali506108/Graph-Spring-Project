package org.neoj4.movieservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AsyncConfig {


    @Bean("movieExecutor")
    public Executor executor() {
        return new ThreadPoolExecutor(
                8,
                16,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadFactory() {
                    private final AtomicInteger count = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread rt = new Thread(r);
                        rt.setName("movie-actor-"   + count.getAndIncrement());
                        rt.setDaemon(true);
                        return rt;
                    }
                }
        );
    }
}
