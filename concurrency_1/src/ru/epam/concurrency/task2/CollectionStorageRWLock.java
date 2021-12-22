package ru.epam.concurrency.task2;

import ru.epam.concurrency.ConcurrencyUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CollectionStorageRWLock {

    private final List<Integer> list = new LinkedList<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public CollectionStorageRWLock() {
    }


    public void printSum() {
        lock.readLock().lock();
        try {
            Integer sum = list.stream().reduce(0, Integer::sum);
            System.out.printf("Sum of ints in collection: %d \n", sum);
        } finally {
            lock.readLock().unlock();
            ConcurrencyUtils.sleep(TimeUnit.MILLISECONDS, 300);
        }
    }

    public void printSquareSum() {
        lock.readLock().lock();
        try {
            Double squareSum = list.stream()
                    .map(num -> (Math.pow(num, 2)))
                    .reduce(0.0, Double::sum);

            System.out.printf("Square root of sum of squares: %f \n", Math.sqrt(squareSum));
        } finally {
            lock.readLock().unlock();
            ConcurrencyUtils.sleep(TimeUnit.MILLISECONDS, 300);
        }
    }

    public void write(Integer integer) {
        lock.writeLock().lock();
        try {
            list.add(integer);
        } finally {
            lock.writeLock().unlock();
            ConcurrencyUtils.sleep(TimeUnit.MILLISECONDS, 100);
        }
    }

}
