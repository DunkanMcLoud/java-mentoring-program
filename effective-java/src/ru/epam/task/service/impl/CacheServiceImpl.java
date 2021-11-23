package ru.epam.task.service.impl;

import ru.epam.task.domain.CacheLifeTime;
import ru.epam.task.domain.CacheStatistics;
import ru.epam.task.domain.DNASequence;
import ru.epam.task.domain.notification.RemoveEventNotification;
import ru.epam.task.service.CacheService;
import ru.epam.task.service.events.EventType;
import ru.epam.task.service.events.listeners.RemovalListener;
import ru.epam.task.service.events.manager.ServiceEventManager;

import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CacheServiceImpl implements CacheService<String, DNASequence> {

    private int maximumSize;
    private CacheLifeTime maxLifeTime;
    private final ConcurrentHashMap<String, DNASequence> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalTime> entriesCreationTime = new ConcurrentHashMap<>();
    private final CacheStatistics cacheStatistics = new CacheStatistics();
    private CacheLifeTimeEvictionHandler<String, DNASequence> cacheLifeTimeEvictor;

    @Override
    public Optional<DNASequence> get(String key) {
        Optional<DNASequence> result;
        if (cache.containsKey(key)) {
            if (cacheLifeTimeEvictor != null) {
                if (cacheLifeTimeEvictor.cancelTimeEviction(key)) {
                    cacheLifeTimeEvictor.handleTimeEviction(key);
                    result = Optional.of(cache.get(key));
                    entriesCreationTime.replace(key, LocalTime.now());
                    return result;
                }
            }
            result = Optional.of(cache.get(key));
            entriesCreationTime.replace(key, LocalTime.now());
            return result;
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Boolean put(String proteinName, DNASequence sequence) {
        if (cache.size() >= maximumSize) {
            this.evictOldestEntry();
        }
        long startTime = System.nanoTime();
        cache.put(proteinName, sequence);
        cacheStatistics.newInputTime(System.nanoTime() - startTime);
        entriesCreationTime.put(proteinName, LocalTime.now());
        if (cacheLifeTimeEvictor != null) {
            cacheLifeTimeEvictor.handleTimeEviction(proteinName);
        }
        return true;
    }

    private void evictOldestEntry() {
        String oldestKey = entriesCreationTime.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .get()
                .getKey();
        if (cacheLifeTimeEvictor != null) {
            if (cacheLifeTimeEvictor.cancelTimeEviction(oldestKey)) {
                removeKeyAndSendNotification(oldestKey);
            }
        } else {
            removeKeyAndSendNotification(oldestKey);
        }
    }

    private void removeKeyAndSendNotification(String key) {
        cache.remove(key);
        cacheStatistics.newEviction();
        ServiceEventManager.getInstance().notify(RemoveEventNotification.of(key));
    }


    @Override
    public CacheStatistics getStatistics() {
        return cacheStatistics;
    }

    public static CacheServiceImplBuilder builder() {
        return new CacheServiceImplBuilder();
    }

    public void setMaximumSize(int size) {
        this.maximumSize = size;
    }

    public void setMaxLifeTime(TimeUnit timeUnit, int quantity) {
        this.maxLifeTime = new CacheLifeTime(timeUnit, quantity);
        this.cacheLifeTimeEvictor = new CacheLifeTimeEvictionHandler<>(cache, entriesCreationTime, this.maxLifeTime, cacheStatistics);
    }


    static class CacheServiceImplBuilder {

        private final CacheServiceImpl cacheService;


        public CacheServiceImplBuilder() {
            this.cacheService = new CacheServiceImpl();
        }

        public CacheServiceImplBuilder setMaximumSize(int maximumSize) {
            this.cacheService.setMaximumSize(maximumSize);
            return this;
        }

        public CacheServiceImplBuilder setRemovalListener(RemovalListener listener) {
            ServiceEventManager.getInstance().subscribe(EventType.REMOVE, listener);
            return this;
        }

        public CacheServiceImplBuilder setMaxLifeTime(TimeUnit timeUnit, int quantity) {
            this.cacheService.setMaxLifeTime(timeUnit, quantity);
            return this;
        }

        public CacheServiceImpl build() {
            return this.cacheService;
        }
    }

}
