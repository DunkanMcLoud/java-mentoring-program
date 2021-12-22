package ru.epam.concurrency2.task3;

import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int DOT_NUMBER_FOR_ANIMATION = 10;
    private static final String ERASER = new String(new char[DOT_NUMBER_FOR_ANIMATION + 1]).replace("\0", "\b");

    public static void main(String[] args) {
        breakOnPathInputAbsence(args);
        runAsyncScan(args[0]);
    }

    private static void runAsyncScan(String directoryForScan) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        FileScanningTask task = new FileScanningTask(Paths.get(directoryForScan).toFile());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!task.isDone()) {
                System.out.println("\n Process interrupted!\nShutting down...\n");
                DirectoryStats currentStats = task.getStats();
                task.cancel(true);
                System.out.println(currentStats.toString());
            } else {
                System.out.println("\n Scanning complete: \n");
                System.out.println(task.getStats().toString());
            }
        }));

        forkJoinPool.execute(task);

        System.out.println("Waiting for complete...\n");
        System.out.println("Press [ Ctrl + C ] to interrupt...\n");
        while (!task.isDone()) {
            showExecutionAnimation();
        }

        task.join();
    }

    private static void showExecutionAnimation() {
        for (int i = 0; i <= DOT_NUMBER_FOR_ANIMATION; i++) {
            if (i == DOT_NUMBER_FOR_ANIMATION) {
                System.out.print(ERASER);
            } else {
                System.out.print(".");
                sleep();
            }
        }
    }

    private static void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(216);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void breakOnPathInputAbsence(String[] args) {
        if (args.length == 0) {
            System.out.println("Please, provide a path for scanning as an argument and try again!\n");
            System.exit(0);
        }
    }

}
