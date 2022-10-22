package it.stredox02.chattranslator.bukkit.scheduler;

import it.stredox02.chattranslator.common.scheduler.AbstractScheduler;
import it.stredox02.chattranslator.common.scheduler.AsyncScheduler;
import it.stredox02.chattranslator.common.scheduler.SyncScheduler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BukkitSchedulerImpl extends AbstractScheduler {

    private final JavaPlugin javaPlugin;

    @Override
    public SyncScheduler getSyncScheduler() {
        return new SyncScheduler() {
            @Override
            public void run(@NotNull Runnable runnable) {
                Bukkit.getScheduler().runTask(javaPlugin,runnable);
            }

            @Override
            public void runLater(@NotNull Runnable runnable, int ticks) {
                Bukkit.getScheduler().runTaskLater(javaPlugin,runnable,ticks);
            }

            @Override
            public void runTimer(@NotNull Runnable runnable, int initialDelay, int repeatingDelay) {
                Bukkit.getScheduler().runTaskTimer(javaPlugin,runnable,initialDelay,repeatingDelay);
            }
        };
    }

    @Override
    public AsyncScheduler getAsyncScheduler() {
        return new AsyncScheduler() {
            @Override
            public void run(@NotNull Runnable runnable) {
                Bukkit.getScheduler().runTaskAsynchronously(javaPlugin,runnable);
            }

            @Override
            public void runLater(@NotNull Runnable runnable, int ticks) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(javaPlugin,runnable,ticks);
            }

            @Override
            public void runTimer(@NotNull Runnable runnable, int initialDelay, int repeatingDelay) {
                Bukkit.getScheduler().runTaskTimerAsynchronously(javaPlugin,runnable, initialDelay,repeatingDelay);
            }
        };
    }

}
