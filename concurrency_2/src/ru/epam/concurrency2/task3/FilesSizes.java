package ru.epam.concurrency2.task3;

import java.nio.file.Path;

public enum FilesSizes {

    MEGABYTES(1024L * 1024L),
    KILOBYTES(1024L);

    private final long value;

    FilesSizes(long l) {
        this.value = l;
    }

    public long fromBytes(long bytes) {
        return Long.divideUnsigned(bytes, value);
    }

    public long ofFile(Path path) {
        return path.toFile().length() / value;
    }
}
