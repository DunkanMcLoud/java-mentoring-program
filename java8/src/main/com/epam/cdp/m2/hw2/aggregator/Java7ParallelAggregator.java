package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class Java7ParallelAggregator<T> implements Aggregator {

    private static final int THREAD_NUM = Runtime.getRuntime().availableProcessors();

    public static final Comparator<Map.Entry<String, AtomicLong>> FREQ_COMPARATOR = new Comparator<Map.Entry<String, AtomicLong>>() {
        @Override
        public int compare(Map.Entry<String, AtomicLong> entry1, Map.Entry<String, AtomicLong> entry2) {
            int compareResult;
            compareResult = Long.compare(entry2.getValue().get(), entry1.getValue().get());
            if (compareResult == 0) {
                compareResult = entry1.getKey().compareToIgnoreCase(entry2.getKey());
            }
            return compareResult;
        }
    };

    public static final Comparator<Map.Entry<String, AtomicLong>> DUPLICATES_COMPARATOR = new Comparator<Map.Entry<String, AtomicLong>>() {
        @Override
        public int compare(Map.Entry<String, AtomicLong> entry1, Map.Entry<String, AtomicLong> entry2) {
            int compareResult;
            compareResult = Integer.compare(entry1.getKey().length(), entry2.getKey().length());
            if (compareResult == 0) {
                compareResult = entry1.getKey().compareToIgnoreCase(entry2.getKey());
            }
            return compareResult;
        }
    };

    @Override
    public int sum(List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return 0;
        }
        AtomicInteger result = new AtomicInteger();
        List<List<?>> chunksFromList = getChunksForThreadsFromList(numbers);

        //thread quantity equivalent for chunk sizes
        CountDownLatch countDownLatch = new CountDownLatch(chunksFromList.size());
        ExecutorService es = Executors.newFixedThreadPool(chunksFromList.size());

        for (List<?> objects : chunksFromList) {
            es.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (Object object : objects) {
                            Integer num = (Integer) object;
                            result.getAndAdd(num);
                        }
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }
        awaitCountDownLatch(countDownLatch);
        return result.get();
    }


    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        if (words.isEmpty()) {
            return Collections.emptyList();
        }
        if (words.size() == 1) {
            return Collections.singletonList(new Pair<>(words.get(0), 1L));
        }

        List<List<?>> chunksFromList = getChunksForThreadsFromList(words);

        //thread quantity equivalent for chunk sizes
        ConcurrentHashMap<String, AtomicLong> freqMap = getWordOccurenceMap(chunksFromList, false);

        //sequential execution with same logic as standard aggregator
        List<Map.Entry<String, AtomicLong>> entries = mapEntrySetToList(freqMap);

        Collections.sort(entries, FREQ_COMPARATOR);
        List<Pair<String, Long>> result = new LinkedList<>();
        int splitQuantity = Math.min(Math.toIntExact(limit), entries.size());
        List<Map.Entry<String, AtomicLong>> limitedEntries = entries.subList(0, splitQuantity);
        for (
                Map.Entry<String, AtomicLong> entry : limitedEntries) {
            Pair<String, Long> resultPair = new Pair<>(entry.getKey(), entry.getValue().get());
            result.add(resultPair);
        }
        return result;
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        if (words.isEmpty() || words.size() == 1) {
            return Collections.emptyList();
        }
        List<List<?>> chunksFromList = getChunksForThreadsFromList(words);
        ConcurrentHashMap<String, AtomicLong> freqMap = getWordOccurenceMap(chunksFromList, true);

        //sequential execution with same logic as standard aggregator
        List<Map.Entry<String, AtomicLong>> entries = mapEntrySetToList(freqMap);

        Collections.sort(entries, DUPLICATES_COMPARATOR);

        List<String> duplicates = new LinkedList<>();
        for (Map.Entry<String, AtomicLong> entry : entries) {
            if (entry.getValue().get() > 1 && limit != 0) {
                duplicates.add(entry.getKey());
                limit--;
            }
        }
        return duplicates;
    }

    private ConcurrentHashMap<String, AtomicLong> getWordOccurenceMap(List<List<?>> chunksFromList, boolean shouldUpperCase) {
        //thread quantity equivalent for chunk sizes
        CountDownLatch countDownLatch = new CountDownLatch(Math.min(THREAD_NUM, chunksFromList.size()));
        ExecutorService es = Executors.newFixedThreadPool(Math.min(THREAD_NUM, chunksFromList.size()));

        ConcurrentHashMap<String, AtomicLong> freqMap = new ConcurrentHashMap<>();

        for (
                List<?> objects : chunksFromList) {
            es.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        fillWordOccurenceMap((List<String>) objects, shouldUpperCase, freqMap);
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        awaitCountDownLatch(countDownLatch);
        return freqMap;
    }


    private void fillWordOccurenceMap(List<String> words, boolean shouldUpperCaseKeys,
                                      ConcurrentHashMap<String, AtomicLong> wordQuantityMap) {
        for (String word : words) {
            String caseAdjustedWord = shouldUpperCaseKeys ? word.toUpperCase(Locale.ROOT) : word.toLowerCase(Locale.ROOT);
            wordQuantityMap.computeIfAbsent(caseAdjustedWord, new Function<String, AtomicLong>() {
                @Override
                public AtomicLong apply(String key) {
                    return new AtomicLong(0);
                }
            }).incrementAndGet();
        }
    }

    private List<List<?>> getChunksForThreadsFromList(List<?> list) {
        int listSize = list.size();

        //step could not be 0
        int step = Math.max((int) Math.floor((double) (listSize / THREAD_NUM)), 1);
        int startSliceIndex = 0;
        int endSliceIndex = startSliceIndex + step;
        List<List<?>> chunkList = new ArrayList<>();

        while (endSliceIndex <= (listSize - step)) {
            chunkList.add(list.subList(startSliceIndex, endSliceIndex));
            startSliceIndex = endSliceIndex;
            endSliceIndex += step;
            if ((endSliceIndex >= listSize) || (chunkList.size() - 1) == Java7ParallelAggregator.THREAD_NUM) {
                chunkList.add(list.subList(startSliceIndex, listSize));
                //startSliceIndex = endSliceIndex;
                //endSliceIndex += step;
            }

        }

        return chunkList;
    }


    private void awaitCountDownLatch(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<Map.Entry<String, AtomicLong>> mapEntrySetToList(Map<String, AtomicLong> wordQuantityMap) {
        Object[] objects = wordQuantityMap.entrySet().toArray();
        List<Map.Entry<String, AtomicLong>> castedEntries = new ArrayList<>();

        for (Object object : objects) {
            if (object instanceof Map.Entry) {
                castedEntries.add(((Map.Entry<String, AtomicLong>) object));
            }
        }
        return castedEntries;
    }

}
