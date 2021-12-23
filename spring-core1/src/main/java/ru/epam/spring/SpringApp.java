package ru.epam.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.epam.spring.domain.storage.EntityStorage;

public class SpringApp {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        EntityStorage storage = context.getBean("storage", EntityStorage.class);
        System.out.println(storage);
    }
}
