package ru.epam.spring.domain.dao;

public interface EntityDao<T> {

    T store(T entity);

    T retrieveById(long id);

    Class<T> getEntityType();

    T update(T entity);

    boolean delete(long id);
}
