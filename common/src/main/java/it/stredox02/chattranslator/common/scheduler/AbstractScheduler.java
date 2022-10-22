package it.stredox02.chattranslator.common.scheduler;

public abstract class AbstractScheduler {

    public abstract SyncScheduler getSyncScheduler();

    public abstract AsyncScheduler getAsyncScheduler();

}
