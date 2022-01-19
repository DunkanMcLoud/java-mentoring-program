package ru.epam.spring.util;

import java.util.List;

public class Utils {
    private Utils() {
    }

    public static <T> List<T> getElementsForPage(List<T> elements, int pageSize, int pageNum) {
        if (elements.size() >= pageSize * pageNum) {
            return elements.subList(pageSize * (pageNum - 1), pageSize * pageNum);
        } else {
            if (elements.size() <= pageSize) {
                return elements;
            }
            return elements.subList(pageSize * (pageNum - 1), elements.size());
        }
    }
}
