package ru.epam.spring.domain.dao.impl;

import ru.epam.spring.beans.Dao;
import ru.epam.spring.domain.dao.EntityDao;
import ru.epam.spring.domain.model.User;
import ru.epam.spring.domain.storage.EntityStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Dao
public class UserDao implements EntityDao<User> {

    private static long ID_GEN = 0L;
    private final EntityStorage entityStorage;

    public UserDao(EntityStorage entityStorage) {
        this.entityStorage = entityStorage;
    }

    @Override
    public User store(User user) {
        user.setId(++ID_GEN);
        entityStorage.storeEntity(user,User.class);
        return user;
    }

    @Override
    public User retrieveById(long id) {
        return Optional.of(entityStorage.getEntityById(User.class,id))
        .filter(User.class::isInstance)
        .map(User.class::cast)
        .orElse(null);
    }

    @Override
    public Class<User> getEntityType() {
        return User.class;
    }

    @Override
    public User update(User entity) {
        entityStorage.updateEntity(entity, User.class);
        return entity;
    }

    @Override
    public boolean delete(long id) {
        return entityStorage.deleteEntityById(User.class,id);
    }

    public User getUserByEmail(String email) {
        return entityStorage.getAllEntitiesOfType(User.class)
                .stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .filter(user -> email.equals(user.getEmail()))
                .findAny()
                .orElse(null);
    }

    public List<User> getUsersByName(String name) {
        return entityStorage.getAllEntitiesOfType(User.class)
                .stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .filter(user -> name.equals(user.getName()))
                .collect(Collectors.toList());
    }
}
