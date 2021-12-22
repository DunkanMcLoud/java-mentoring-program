package ru.epam.concurrency2.task1;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

public class FactorialTask extends RecursiveTask<BigInteger> {

    private final BigInteger start;
    private final BigInteger end;

    private BigInteger result = BigInteger.ONE;

    private static final BigInteger DIVISOR = BigInteger.valueOf(2);

    private FactorialTask(BigInteger start, BigInteger end) {
        this.start = start;
        this.end = end;
    }

    public FactorialTask(BigInteger arg) {
        this(BigInteger.ONE, arg);
    }


    @Override
    protected BigInteger compute() {
        if (start.equals(end)) {
            return end;
        } else {
            BigInteger middle = start.add(end.subtract(start).divide(DIVISOR));

            FactorialTask left = new FactorialTask(start, middle);
            FactorialTask right = new FactorialTask(middle.add(BigInteger.ONE), end);

            invokeAll(left, right);

            result = left.getRawResult().multiply(right.getRawResult());
            return result;
        }
    }
}
