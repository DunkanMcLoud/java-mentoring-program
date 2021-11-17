package ru.epam.task.service.impl;

import org.junit.Test;
import ru.epam.task.domain.CacheStatistics;
import ru.epam.task.domain.DNASequence;
import ru.epam.task.service.CacheService;
import ru.epam.task.service.events.listeners.RemovalListener;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CacheServiceTest {

    private static final String PROTEIN_1 = "x-fc4";
    private static final String PROTEIN_2 = "y-fg3";
    private static final String PROTEIN_3 = "z-gi3";
    private static final DNASequence TEST_SEQUENCE_1 = DNASequence.create("atcgaccga");
    private static final DNASequence TEST_SEQUENCE_2 = DNASequence.create("atggaccga");
    private static final DNASequence TEST_SEQUENCE_3 = DNASequence.create("agcgaccga");
    private static final Random RANDOMIZER = new Random();

    private CacheService<String, DNASequence> cacheService;

    @Test
    public void testEvictionPolicyBySize() throws InterruptedException {
        cacheService = CacheServiceImpl.builder()
                .setMaximumSize(2)
                .setMaxLifeTime(TimeUnit.SECONDS, 5)
                .setRemovalListener(new RemovalListener())
                .build();

        //when
        cacheService.put(PROTEIN_1, TEST_SEQUENCE_1);

        TimeUnit.MILLISECONDS.sleep(100);

        cacheService.put(PROTEIN_2, TEST_SEQUENCE_2);

        TimeUnit.MILLISECONDS.sleep(100);
        Boolean isOk = cacheService.put(PROTEIN_3, TEST_SEQUENCE_3);
        assertTrue(isOk);

        //then
        assertFalse(cacheService.get(PROTEIN_1).isPresent());

        assertTrue(cacheService.get(PROTEIN_2).isPresent());
        assertTrue(cacheService.get(PROTEIN_3).isPresent());
    }


    @Test
    public void testLoggingOnRemoval() throws InterruptedException {
        // redirecting log output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outContent);
        System.setOut(printStream);

        cacheService = CacheServiceImpl.builder()
                .setMaximumSize(2)
                .setMaxLifeTime(TimeUnit.MILLISECONDS, 500)
                .setRemovalListener(new RemovalListener())
                .build();

        //when
        cacheService.put(PROTEIN_1, TEST_SEQUENCE_1);
        TimeUnit.SECONDS.sleep(1);

        //then
        assertFalse(cacheService.get(PROTEIN_1).isPresent());
        assertTrue(outContent.toString().contains(String.format("%s sequence was removed", PROTEIN_1)));
    }

    @Test
    public void viewStatisticsTest() throws InterruptedException {

        cacheService = CacheServiceImpl.builder()
                .setMaximumSize(2)
                .setMaxLifeTime(TimeUnit.MILLISECONDS, 500)
                .build();

        //when
        cacheService.put(PROTEIN_1, TEST_SEQUENCE_1);
        cacheService.put(PROTEIN_2, TEST_SEQUENCE_2);
        cacheService.put(PROTEIN_3, TEST_SEQUENCE_3);

        TimeUnit.SECONDS.sleep(2);

        //then
        CacheStatistics statistics = cacheService.getStatistics();
        long avInputTime = statistics.getAvgInputTime();
        long cacheEvictionsNum = statistics.getCacheEvictionsNum();

        assertTrue(avInputTime > 0);
        assertEquals(3, cacheEvictionsNum);
    }

    @Test
    public void testLargeDataLoad() throws InterruptedException {
        cacheService = CacheServiceImpl.builder()
                .setMaximumSize(100_000)
                .setMaxLifeTime(TimeUnit.SECONDS, 20)
                .build();

        //when
        Set<DNASequence> resultSet = new HashSet<>();

        //putting values which will be periodically requested during flow
        cacheService.put(PROTEIN_1, TEST_SEQUENCE_1);
        cacheService.put(PROTEIN_2, TEST_SEQUENCE_2);
        cacheService.put(PROTEIN_3, TEST_SEQUENCE_3);

        // put random sequence in cache or request value
        for (int i = 0; i < 200_000; i++) {
            boolean shouldPut = RANDOMIZER.nextBoolean();
            if (shouldPut) {
                byte[] array = new byte[10000];
                RANDOMIZER.nextBytes(array);
                DNASequence newSequence = DNASequence.create(new String(array, StandardCharsets.UTF_8));
                cacheService.put(String.format("PROTEIN_%d", i), newSequence);
            } else {
                int index = RANDOMIZER.nextInt(3);
                Optional<DNASequence> dnaSequence = Optional.empty();
                if (index == 0) {
                    dnaSequence = cacheService.get(PROTEIN_1);
                }
                if (index == 1) {
                    dnaSequence = cacheService.get(PROTEIN_2);
                }
                if (index == 2) {
                    dnaSequence = cacheService.get(PROTEIN_3);
                }
                dnaSequence.ifPresent(resultSet::add);
            }
            // watch average input time change
            if (i == 50_000) {
                System.out.printf("AvgInputTime : %s%n", cacheService.getStatistics().getAvgInputTime());
            }
        }

        TimeUnit.SECONDS.sleep(10);
        //then
        CacheStatistics statistics = cacheService.getStatistics();
        System.out.printf("AvgInputTime : %s%n", statistics.getAvgInputTime());
        System.out.printf("CacheEveictionsNuber : %s%n", statistics.getCacheEvictionsNum());
        assertTrue(statistics.getCacheEvictionsNum() > 10);


        assertEquals(3, resultSet.size());
        assertTrue(cacheService.get(PROTEIN_1).isPresent());
        assertTrue(cacheService.get(PROTEIN_2).isPresent());
        assertTrue(cacheService.get(PROTEIN_3).isPresent());
    }
}
