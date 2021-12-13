package ru.epam.concurrency.task1;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Task1 {

    @Test
    public void simpleHashMap_assertThrows_ConcurrentModificationException() {
        Map<Integer, Integer> simpleMap = new HashMap<>();

        Thread writerThread = new Thread(() -> {
            while (true) {
                int random = ThreadLocalRandom.current().nextInt();
                simpleMap.put(random, random);
            }
        });

        Thread sumThread = new Thread(() -> {
            assertThrows(ConcurrentModificationException.class, () -> {
                while (true) {
                    Integer sum = simpleMap.values()
                            .stream()
                            .reduce(0, Integer::sum);
                    simpleMap.put(sum, sum);
                }
            });
        });

        writerThread.start();
        sumThread.start();
    }

    @Test
    public void concurrentHashMapShouldImprove() throws InterruptedException {
        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        Thread writerThread = new Thread(() -> {
            while (true) {
                int random = ThreadLocalRandom.current().nextInt();
                concurrentHashMap.putIfAbsent(random, random);
            }
        });

        Thread sumThread = new Thread(() -> {
            while (true) {
                Integer sum = concurrentHashMap.values()
                        .stream()
                        .reduce(0, Integer::sum);
                concurrentHashMap.putIfAbsent(sum, sum);
            }
        });

        writerThread.start();
        sumThread.start();

        TimeUnit.SECONDS.sleep(10);

        System.out.println(concurrentHashMap.size());
    }

    @Test
    public void syncronizedMapShould_notImprove() throws InterruptedException {
        Map<Integer, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<>());

        Thread writerThread = new Thread(() -> {
            while (true) {
                int random = ThreadLocalRandom.current().nextInt();
                synchronizedMap.putIfAbsent(random, random);
            }
        });

        Thread sumThread = new Thread(() -> {
            assertThrows(ConcurrentModificationException.class, () -> {
                while (true) {
                    Integer sum = synchronizedMap.values()
                            .stream()
                            .reduce(0, Integer::sum);
                    synchronizedMap.putIfAbsent(sum, sum);
                }
            });
        });


        writerThread.start();
        sumThread.start();

    }

}
