package ru.epam.concurrency2.task2;

import java.math.BigInteger;
import java.util.concurrent.RecursiveAction;

public class ForkJoinQuickSort {

    static class ForkJoinQuickSortAction extends RecursiveAction {
        private static final int SEQUENTIAL_THRESHOLD = 10;
        private final BigInteger[] data;
        private final int fromInclusive;
        private final int toInclusive;

        public ForkJoinQuickSortAction(BigInteger[] data) {
            this(data, 0, data.length - 1);
        }

        private ForkJoinQuickSortAction(BigInteger[] data, int fromInclusive, int toInclusive) {
            this.data = data;
            this.fromInclusive = fromInclusive;
            this.toInclusive = toInclusive;
        }

        @Override
        protected void compute() {
            if (toInclusive <= fromInclusive) {
                return;
            } else {
                int pivot = partition(data, fromInclusive, toInclusive);
                ForkJoinQuickSortAction left = new ForkJoinQuickSortAction(data, fromInclusive, pivot);
                ForkJoinQuickSortAction right = new ForkJoinQuickSortAction(data, pivot + 1, toInclusive);

                invokeAll(left, right);
            }
        }

        private int partition(BigInteger[] array, int fromInclusive, int toInclusive) {
            System.out.println(Thread.currentThread().getName());
            BigInteger pivot = array[fromInclusive];
            int i = fromInclusive - 1;
            int j = toInclusive + 1;
            while (true) {
                //while elements less then pivot
                do {
                    ++i;
                } while ((array[i].compareTo(pivot) < 0));
                //while elements are greater then pivot
                do {
                    j--;
                }
                while ((array[j].compareTo(pivot) > 0));
                if (i >= j) {
                    return j;
                }
                BigInteger temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
    }
}
