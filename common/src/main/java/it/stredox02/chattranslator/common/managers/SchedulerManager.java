package it.stredox02.chattranslator.common.managers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SchedulerManager {

    private static SchedulerManager instance;

    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    public static SchedulerManager getInstance() {
        if(instance == null){
            instance = new SchedulerManager();
        }
        return instance;
    }

    public void scheduleNow(@NotNull Runnable runnable){
        forkJoinPool.execute(runnable);
    }

    public Future<?> scheduleRepeating(@NotNull Runnable runnable, int delay, int repeat, @NotNull TimeUnit timeUnit){
        return scheduler.scheduleAtFixedRate(runnable,delay,repeat,timeUnit);
    }

    public void shutdown(){
        forkJoinPool.shutdownNow();
        scheduler.shutdownNow();
        try {
            forkJoinPool.awaitTermination(30, TimeUnit.SECONDS);
            scheduler.awaitTermination(30,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
