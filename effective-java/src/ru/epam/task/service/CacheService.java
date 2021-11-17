package ru.epam.task.service;

import ru.epam.task.domain.CacheStatistics;

import java.util.Optional;

public interface CacheService<K, V> {

    Optional<V> get(K key);

    Boolean put(K key, V value);
    
    CacheStatistics getStatistics();

}
