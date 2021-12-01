package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Java7Aggregator implements Aggregator {

    private static final Comparator<Map.Entry<String, Long>> FREQ_COMPARATOR = new Comparator<Map.Entry<String, Long>>() {
        @Override
        public int compare(Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) {
            int compareResult;
            compareResult = entry2.getValue().compareTo(entry1.getValue());
            if (compareResult == 0) {
                compareResult = entry1.getKey().compareToIgnoreCase(entry2.getKey());
            }
            return compareResult;
        }
    };

    private static final Comparator<Map.Entry<String, Long>> DUPLICATES_COMPARATOR = new Comparator<Map.Entry<String, Long>>() {
        @Override
        public int compare(Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) {
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
        int result = 0;
        for (Integer integer : numbers) {
            result += integer;
        }
        return result;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        List<Map.Entry<String, Long>> sortedEntries = getSortedWordQuantityEntriesInAscendingOrder(words, false, FREQ_COMPARATOR);
        List<Pair<String, Long>> result = new LinkedList<>();
        int splitQuantity = Math.min(Math.toIntExact(limit), sortedEntries.size());
        List<Map.Entry<String, Long>> limitedEntries = sortedEntries.subList(0, splitQuantity);
        for (Map.Entry<String, Long> entry : limitedEntries) {
            Pair<String, Long> resultPair = new Pair<>(entry.getKey(), entry.getValue());
            result.add(resultPair);
        }
        return result;
    }


    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        List<Map.Entry<String, Long>> sortedEntries = getSortedWordQuantityEntriesInAscendingOrder(words, true, DUPLICATES_COMPARATOR);
        List<String> duplicates = new LinkedList<>();
        for (Map.Entry<String, Long> entry : sortedEntries) {
            if (entry.getValue() > 1 && limit != 0) {
                duplicates.add(entry.getKey());
                limit--;
            }
        }
        return duplicates;
    }

    private List<Map.Entry<String, Long>> getSortedWordQuantityEntriesInAscendingOrder(List<String> words, boolean shouldUpperCaseKeys,
                                                                                       Comparator<Map.Entry<String, Long>> comparator) {
        Map<String, Long> wordQuantityMap = new HashMap<>();
        for (String word : words) {
            String caseAdjustedWord = shouldUpperCaseKeys ? word.toUpperCase(Locale.ROOT) : word.toLowerCase(Locale.ROOT);
            if (!wordQuantityMap.containsKey(caseAdjustedWord)) {
                wordQuantityMap.put(caseAdjustedWord, 1L);
            } else {
                Long occurenceNumber = wordQuantityMap.get(caseAdjustedWord);
                wordQuantityMap.put(caseAdjustedWord, ++occurenceNumber);
            }
        }

        Object[] objects = wordQuantityMap.entrySet().toArray();
        List<Map.Entry<String, Long>> castedEntries = new ArrayList<>();

        for (Object object : objects) {
            if (object instanceof Map.Entry) {
                castedEntries.add(((Map.Entry<String, Long>) object));
            }
        }
        Collections.sort(castedEntries, comparator);
        return castedEntries;
    }
}
