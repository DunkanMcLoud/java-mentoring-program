package ru.epam.concurrency.task2;

import java.util.Collection;

public class CollectionStorage {
    private volatile Collection<Integer> collection;
    private final Object readLock = new Object();
    private final Object writeLock = new Object();
    private volatile int readerNum = 0;

    public CollectionStorage(Collection<Integer> collection) {
        this.collection = collection;
    }


    public void printSum() {
        synchronized (writeLock) {
            readerNum++;
        }
        synchronized (readLock) {
            if (--readerNum == 0) {
                Integer sum = collection.stream().reduce(0, Integer::sum);
                System.out.printf("Sum of ints in collection: %d \n", sum);
                readLock.notify();
            }
        }
    }

    public void printSquareSum() {
        synchronized (writeLock) {
            readerNum++;
        }
        synchronized (readLock) {
            if (--readerNum == 0) {
                Double squareSum = collection.stream()
                        .map(num -> (Math.pow(num, 2)))
                        .reduce(0.0, Double::sum);
                System.out.printf("Square root of sum of squares: %f \n", Math.sqrt(squareSum));
                readLock.notify();
            }
        }
    }

    public void write(Integer integer) {
        synchronized (writeLock) {
            synchronized (readLock) {
                while (readerNum != 0) {
                    waitLock(readLock);
                    collection.add(integer);
                }
            }
        }
    }

    private void waitLock(Object monitor) {
        try {
            monitor.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
