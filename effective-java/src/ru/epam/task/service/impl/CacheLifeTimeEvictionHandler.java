package ru.epam.task.service.impl;

import ru.epam.task.domain.CacheLifeTime;
import ru.epam.task.domain.CacheStatistics;
import ru.epam.task.service.events.manager.ServiceEventManager;
import ru.epam.task.domain.notification.RemoveEventNotification;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public final class CacheLifeTimeEvictionHandler<K, V> {

    private final ConcurrentHashMap<K, V> cache;
    private final ConcurrentHashMap<K, LocalTime> lifeTimeTable;
    private final ConcurrentHashMap<K, ScheduledFuture<?>> jobs = new ConcurrentHashMap<>();
    private final CacheLifeTime cacheLifeTime;
    private final CacheStatistics cacheStatistics;

    static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(threadFactory -> {
        Thread thread = new Thread(threadFactory);
        thread.setDaemon(true);
        return thread;
    });

    public CacheLifeTimeEvictionHandler(ConcurrentHashMap<K, V> cache, ConcurrentHashMap<K, LocalTime> lifeTimeTable,
                                        CacheLifeTime maxLifeTime, CacheStatistics statistics) {
        this.cache = cache;
        this.lifeTimeTable = lifeTimeTable;
        this.cacheLifeTime = maxLifeTime;
        this.cacheStatistics = statistics;
    }

    public void handleTimeEviction(K key) {
        ScheduledFuture<?> scheduledFuture = SCHEDULER.schedule(() -> {
            cache.remove(key);
            ServiceEventManager.getInstance().notify(RemoveEventNotification.of(key));
            lifeTimeTable.remove(key);
            cacheStatistics.newEviction();
            jobs.remove(key);
        }, cacheLifeTime.getQuantity(), cacheLifeTime.getTimeUnit());
        jobs.put(key, scheduledFuture);
    }

    public boolean cancelTimeEviction(K key) {
        return jobs.get(key).cancel(false);
    }


}
