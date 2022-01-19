package ru.epam.spring.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;
import ru.epam.spring.domain.dao.EntityDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EntityStorageDaoSetter implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
 /*   @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Dao.class)) {
            if (bean instanceof EntityDao) {
                daosMap.put(beanClass, (EntityDao<?>) bean);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof EntityStorage) {
            EntityStorage entityStorage = (EntityStorage) bean;
            Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(entityStorage.getClass());
            List<Method> methods = Arrays.stream(declaredMethods)
                    .filter(method -> Objects.nonNull(method.getDeclaredAnnotation(DaoInjectable.class)))
                    .collect(Collectors.toList());
            methods.forEach(method -> {
                method.setAccessible(true);
                try {
                    method.invoke(bean, daosMap);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
                    method.setAccessible(false);
                }
            });
        }
        return bean;
    }*/

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LOGGER.info("Wiring up dao entities in EntityStorage");
        ru.epam.spring.domain.storage.EntityStorage entityStorage = applicationContext.getBean(ru.epam.spring.domain.storage.EntityStorage.class);
        List<EntityDao> beansOfType = new LinkedList<>(applicationContext.getBeansOfType(EntityDao.class).values());
        Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(entityStorage.getClass());
        List<Method> methods = Arrays.stream(declaredMethods)
                .filter(method -> Objects.nonNull(method.getDeclaredAnnotation(DaoTypesAware.class)))
                .collect(Collectors.toList());
        methods.forEach(method -> {
            method.setAccessible(true);
            try {
                method.invoke(entityStorage, beansOfType);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } finally {
                method.setAccessible(false);
            }
        });
    }

}
