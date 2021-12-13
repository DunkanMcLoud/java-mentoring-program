package ru.epam.concurrency.task3.producer_consumer;

import ru.epam.concurrency.ConcurrencyUtils;

import java.util.concurrent.TimeUnit;

public class MessageConsumer extends Thread {
    private final BlockingQueue<String> messageBus;

    public MessageConsumer(BlockingQueue<String> messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public void run() {
        Thread consumerThread = Thread.currentThread();
        while (!consumerThread.isInterrupted()) {
            try {
                ConcurrencyUtils.sleep(TimeUnit.MILLISECONDS, 300);
                String message = messageBus.take();
                System.out.println(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
