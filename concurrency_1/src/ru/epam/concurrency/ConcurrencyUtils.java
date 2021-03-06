package ru.epam.concurrency;

import java.util.concurrent.TimeUnit;

public class ConcurrencyUtils {
    private ConcurrencyUtils() {

    }

    public static void sleep(TimeUnit timeUnit, int quantity) {
        try {
            timeUnit.sleep(quantity);
        } catch (InterruptedException e) {
            System.out.printf("Sleep was interrupted for thread:[ %s ] \n",Thread.currentThread().getName());
        }
    }
}
