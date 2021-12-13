package ru.epam.concurrency.task3.producer_consumer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static ru.epam.concurrency.ConcurrencyUtils.sleep;

class ProducerConsumer {

    @Test
    public void test() {
        BlockingQueue<String> messageBus = new BlockingQueue<>();

        Thread producer = new Thread(() -> {
            Thread producerThread = Thread.currentThread();
            int i = 1;
            while (!producerThread.isInterrupted()) {
                sleep(TimeUnit.MILLISECONDS, 500);
                messageBus.add(String.format("Message #No: %d", i++));
            }
        });

        Thread consumer = new Thread(() -> {
            Thread consumerThread = Thread.currentThread();
            while (!consumerThread.isInterrupted()) {
                try {
                    sleep(TimeUnit.MILLISECONDS, 300);
                    String message = messageBus.take();
                    System.out.println(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        producer.start();
        consumer.start();

        sleep(TimeUnit.SECONDS, 5);

        producer.interrupt();
        consumer.interrupt();
    }

    @Test
    public void testMultipleProducersAndConsumers() {
        BlockingQueue<String> messageBus = new BlockingQueue<>();

        //given
        MessageProducer messageProducer_1 = new MessageProducer(messageBus);
        MessageProducer messageProducer_2 = new MessageProducer(messageBus);
        MessageProducer messageProducer_3 = new MessageProducer(messageBus);
        MessageConsumer messageConsumer1 = new MessageConsumer(messageBus);
        MessageConsumer messageConsumer2 = new MessageConsumer(messageBus);

        //when
        messageProducer_1.start();
        messageProducer_2.start();
        messageProducer_3.start();
        messageConsumer1.start();
        messageConsumer2.start();

        sleep(TimeUnit.SECONDS, 10);
        //then
        messageProducer_1.interrupt();
        messageProducer_2.interrupt();
        messageProducer_3.interrupt();
        messageConsumer1.interrupt();
        messageConsumer2.interrupt();
    }


}
