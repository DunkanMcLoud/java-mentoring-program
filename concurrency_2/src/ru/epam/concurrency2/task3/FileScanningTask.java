package ru.epam.concurrency2.task3;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

public class FileScanningTask extends RecursiveAction {

    private static final Logger LOGGER = Logger.getLogger(FileScanningTask.class.getName());

    private final DirectoryStats stats;
    private final File currentNode;

    public FileScanningTask(File path) {
        this(DirectoryStats.create(), path);
    }

    private FileScanningTask(DirectoryStats stats, File path) {
        this.stats = stats;
        this.currentNode = path;
    }


    @Override
    protected void compute() {
        if (currentNode.isFile()) {
            stats.increaseMemorySizeStat(currentNode.length());
            stats.addFileToStat();
        } else {
            stats.addFolderToStat();
            Arrays.stream(Objects.requireNonNull(currentNode.listFiles()))
                    .map(file -> new FileScanningTask(stats, file).fork())
                    .forEach(ForkJoinTask::join);
        }
    }

    public DirectoryStats getStats() {
        return stats;
    }
}
