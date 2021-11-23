package ru.epam.task.domain;

public final class CacheStatistics {

    private volatile long cacheEvictionsNumber;
    private volatile long avgInputTime;
    private volatile long numOfInputs;

    public synchronized long getCacheEvictionsNum() {
        return cacheEvictionsNumber;
    }

    public synchronized long getAvgInputTime() {
        return avgInputTime;
    }

    public synchronized void newInputTime(long newTime) {
        long prevSum = this.numOfInputs * this.avgInputTime;
        this.numOfInputs++;
        this.avgInputTime = (prevSum + newTime) / numOfInputs;
    }

    @Override
    public String toString() {
        return String.format("cache evictions: %s %n" +
                "avgInputTime: %s %n", this.cacheEvictionsNumber, this.avgInputTime);
    }

    public synchronized void newEviction() {
        this.cacheEvictionsNumber++;
    }
}
