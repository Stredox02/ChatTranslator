package it.stredox02.chattranslator.common.scheduler;

import org.jetbrains.annotations.NotNull;

public interface AsyncScheduler {

    void run(@NotNull Runnable runnable);

    void runLater(@NotNull Runnable runnable, int ticks);

    void runTimer(@NotNull Runnable runnable, int initialDelay, int repeatingDelay);

}
