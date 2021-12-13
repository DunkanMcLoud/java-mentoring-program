package ru.epam.concurrency.task2;


import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Task2 {

    @Test
    public void threeThreadsInSimpleCollection() throws InterruptedException {
        CollectionStorage collectionStorage = new CollectionStorage(new LinkedList<>());

        Thread writerThread = new Thread(() -> {
            while (true) {
                collectionStorage.write(ThreadLocalRandom.current().nextInt(100));
            }
        });

        Thread sumPrinter = new Thread(() -> {
            while (true) {
                collectionStorage.printSum();
            }
        });

        Thread sqrSumPrinter = new Thread(() -> {
            while (true) {
                collectionStorage.printSquareSum();
            }
        });


        writerThread.start();
        sumPrinter.start();
        sqrSumPrinter.start();

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void threeThreadsInThreadSafeCollection() throws InterruptedException {
        List<Integer> integerList = new CopyOnWriteArrayList<>();

        Thread writerThread = new Thread(() -> {
            while (true) {
                integerList.add(ThreadLocalRandom.current().nextInt(100));
            }
        });

        Thread sumPrinter = new Thread(() -> {
            while (true) {
                Integer sum = integerList.stream()
                        .reduce(0, Integer::sum);
                System.out.printf("Current sum is: %d \n", sum);
            }
        });

        Thread squarePrinter = new Thread(() -> {
            while (true) {
                Double squaresSum = integerList.stream()
                        .map(num -> (Math.pow(num, 2)))
                        .reduce(0.0, Double::sum);
                System.out.printf("Square root of sum of squares: %f \n", Math.sqrt(squaresSum));
            }
        });


        writerThread.start();
        sumPrinter.start();
        squarePrinter.start();

        TimeUnit.SECONDS.sleep(5);
    }

}
