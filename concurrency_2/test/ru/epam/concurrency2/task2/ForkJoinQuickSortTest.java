package ru.epam.concurrency2.task2;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ForkJoinQuickSortTest {


    @Test
    public void testIsSorted() {
        Integer[] ints = {1, 3, 4, 6, 7, 8};
        Integer[] ints2 = {1, 2, 4, 6, 8, 7};

        assertTrue(isSorted(ints));
        assertFalse(isSorted(ints2));
    }

    @Test
    public void testSort() {
        Random random = new Random();
        BigInteger[] bigInts = ThreadLocalRandom.current()
                .ints(0, 32)
                .boxed()
                .limit(1000)
                .map(operand -> new BigInteger(operand, random))
                .toArray(BigInteger[]::new);


        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        ForkJoinQuickSort.ForkJoinQuickSortAction forkJoinQuickSortAction = new ForkJoinQuickSort.ForkJoinQuickSortAction(bigInts);
        forkJoinPool.invoke(forkJoinQuickSortAction);

        forkJoinPool.shutdown();

        assertTrue(isSorted(bigInts));
    }

    private static boolean isSorted(Comparable[] array) {
        for (int i = 1; i <= array.length - 1; i++) {
            if (isLess(array[i], array[i - 1])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isLess(Comparable item1, Comparable item2) {
        if (item1 == item2) {
            return false;
        }
        return item1.compareTo(item2) < 0;
    }

}
