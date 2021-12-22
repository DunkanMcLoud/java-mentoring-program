package ru.epam.concurrency2.task1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FactorialTest {

    @Parameterized.Parameter
    public BigInteger toCount;


    @Parameterized.Parameters
    public static List<BigInteger> factorialNumber() {
        return Arrays.asList(
                BigInteger.valueOf(100),
                BigInteger.valueOf(1000),
                BigInteger.valueOf(10000)
        );
    }


    @Test
    public void factorialTest() throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);

        FactorialTask factorialTask = new FactorialTask(BigInteger.valueOf(15));


        ForkJoinTask<BigInteger> invoke = forkJoinPool.submit(factorialTask);

        assertEquals(invoke.get(), BigInteger.valueOf(1_307_674_368_000L));
    }

    @Test
    public void factorialBigTest() throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);

        FactorialTask factorialTask = new FactorialTask(toCount);

        long fjpStart = System.nanoTime();
        ForkJoinTask<BigInteger> invoke = forkJoinPool.submit(factorialTask);
        BigInteger join = invoke.join();
        long fjpEnd = System.nanoTime();
        long fjpTime = TimeUnit.NANOSECONDS.toMillis(fjpEnd - fjpStart);


        long seqStart = System.nanoTime();
        BigInteger bigInteger = seqFactorial(toCount);
        long seqEnd = System.nanoTime();
        long seqTime = TimeUnit.NANOSECONDS.toMillis(seqEnd - seqStart);


        assertEquals(bigInteger, join);
        System.out.printf("FJP Time (ms): %d ,\n Sequential time (ms): %d \n", fjpTime, seqTime);
    }


    private static BigInteger seqFactorial(BigInteger val) {
        if (val.equals(BigInteger.ONE)) {
            return BigInteger.ONE;
        } else {
            return val.multiply(seqFactorial(val.subtract(BigInteger.ONE)));
        }

    }
}
