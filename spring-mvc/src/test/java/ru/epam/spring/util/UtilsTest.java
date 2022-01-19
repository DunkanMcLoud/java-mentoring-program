package ru.epam.spring.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    @Test
    public void testSplit() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        List<Integer> elementsForPage = Utils.getElementsForPage(integerList, 3, 2);
        List<Integer> elementsForPage2 = Utils.getElementsForPage(integerList, 3, 3);
        List<Integer> elementsForPage3 = Utils.getElementsForPage(integerList, 3, 1);
        List<Integer> elementsForPage4 = Utils.getElementsForPage(integerList, 10, 2);
        List<Integer> elementsForPage5 = Utils.getElementsForPage(Collections.emptyList(), 10, 2);

        assertThat(elementsForPage).containsOnly(4, 5, 6);
        assertThat(elementsForPage2).containsOnly(7, 8);
        assertThat(elementsForPage3).containsOnly(1, 2, 3);
        assertThat(elementsForPage4).containsOnly(1, 2, 3, 4, 5, 6, 7, 8);
        assertThat(elementsForPage5).isEmpty();
    }

}
