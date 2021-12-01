package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Java8ParallelAggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        return numbers.parallelStream()
                .reduce(0, Integer::sum);
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        return words.parallelStream()
                .collect(Collectors.groupingBy(String::toLowerCase, ConcurrentHashMap::new, Collectors.counting()))
                .entrySet()
                .parallelStream()
                .sorted(Comparator.comparingLong(Map.Entry::getValue))
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .limit(limit)
                .map(entry -> (Pair<String, Long>) new Pair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        return words.parallelStream()
                .collect(Collectors.groupingBy(String::toUpperCase, ConcurrentHashMap::new, Collectors.counting()))
                .entrySet()
                .parallelStream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                //could be changed to call for ready Java7 aggregator's comparator
                .sorted((entry1, entry2) -> {
                    int compareResult;
                    compareResult = Integer.compare(entry1.getKey().length(), entry2.getKey().length());
                    if (compareResult == 0) {
                        compareResult = entry1.getKey().compareToIgnoreCase(entry2.getKey());
                    }
                    return compareResult;
                })
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .map(String::toUpperCase)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
