package ru.epam.concurrency.task3.producer_consumer;

import java.util.LinkedList;

public class BlockingQueue<T> {
    private final LinkedList<T> list = new LinkedList<>();

    public boolean add(T element) {
        synchronized (list) {
            list.offer(element);
            list.notify();
        }
        return true;
    }

    public T take() throws InterruptedException {
        synchronized (list) {
            while (list.isEmpty()) {
                list.wait();
            }
            return list.removeFirst();
        }
    }
}
