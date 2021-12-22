package ru.epam.concurrency2.task3;

import java.util.concurrent.atomic.AtomicLong;

public final class DirectoryStats {
    private final AtomicLong fileSizes;
    private final AtomicLong folderCount;
    private final AtomicLong fileCount;

    public static DirectoryStats create() {
        return new DirectoryStats(
                new AtomicLong(0L),
                new AtomicLong(0L),
                new AtomicLong(0L)
        );
    }

    public DirectoryStats(AtomicLong size, AtomicLong folderCount, AtomicLong fileCount) {
        this.fileSizes = size;
        this.folderCount = folderCount;
        this.fileCount = fileCount;
    }


    public void increaseMemorySizeStat(long toAdd) {
        fileSizes.addAndGet(toAdd);
    }

    public void addFolderToStat() {
        folderCount.incrementAndGet();
    }

    public void addFileToStat() {
        fileCount.incrementAndGet();
    }

    @Override
    public String toString() {
        return String.format("Space occupied : %d kb,\n" +
                        "Folders count: %d \n" +
                        "Files number: %d \n",
                FilesSizes.KILOBYTES.fromBytes(fileSizes.get()),
                folderCount.get(),
                fileCount.get());
    }
}
