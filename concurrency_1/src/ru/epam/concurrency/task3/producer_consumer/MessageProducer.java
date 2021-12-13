package ru.epam.concurrency.task3.producer_consumer;

import ru.epam.concurrency.ConcurrencyUtils;

import java.util.concurrent.TimeUnit;

public class MessageProducer extends Thread {

    private final BlockingQueue<String> messageBus;

    public MessageProducer(BlockingQueue<String> messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public void run() {
        Thread producerThread = Thread.currentThread();
        int i = 1;
        while (!producerThread.isInterrupted()) {
            ConcurrencyUtils.sleep(TimeUnit.MILLISECONDS, 500);
            messageBus.add(String.format("Message #No: %d from producer no %d", i++, Thread.currentThread().getId()));
        }
    }


}
