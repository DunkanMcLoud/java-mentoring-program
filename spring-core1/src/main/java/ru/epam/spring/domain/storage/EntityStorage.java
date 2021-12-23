package ru.epam.spring.domain.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.epam.spring.beans.DaoTypesAware;
import ru.epam.spring.domain.dao.EntityDao;
import ru.epam.spring.domain.model.PersistedEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EntityStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityStorage.class);

    private ConcurrentHashMap<Class<? extends PersistedEntity>, ConcurrentHashMap<Long, PersistedEntity>> entitiesMap;

    @DaoTypesAware
    private void createMapOnDaoTypes(List<EntityDao> daos) {
        ConcurrentHashMap<Class<? extends PersistedEntity>, ConcurrentHashMap<Long, PersistedEntity>> map = new ConcurrentHashMap<>();
        daos.forEach(entityDao -> {
            map.put(entityDao.getEntityType(), new ConcurrentHashMap<>());
        });
        this.entitiesMap = map;
        LOGGER.info("Initialized in-memory storage for [{}] DAOs", daos);
    }

    public Object getEntityById(Class<? extends PersistedEntity> clazz, long id) {
        PersistedEntity persistedEntity;
        try {
            persistedEntity = entitiesMap.get(clazz).get(id);
            return persistedEntity;
        } catch (NullPointerException e) {
            LOGGER.error("entity of class {} and id {} is absent",clazz.getName(),id);
            return null;
        }
    }

    public List<PersistedEntity> getAllEntitiesOfType(Class<? extends PersistedEntity> clazz) {
        return new ArrayList<>(entitiesMap.get(clazz)
                .values());
    }

    public void storeEntity(PersistedEntity entity, Class<? extends PersistedEntity> clazz) {
        entitiesMap.get(clazz)
                .put(entity.getId(), entity);
        LOGGER.debug("Stored {} with id {}", entity.getClass(), entity.getId());
    }

    public void updateEntity(PersistedEntity entity, Class<? extends PersistedEntity> clazz) {
        if (entity.getId() == 0) {
            throw new UnsupportedOperationException("You could not update entity without id");
        } else {
            entitiesMap.get(clazz).replace(entity.getId(), entity);
            LOGGER.debug("Updated entity of type {} with id {}", clazz.getName(), entity.getId());
        }
    }

    public boolean deleteEntityById(Class<? extends PersistedEntity> clazz, long id) {
        try {
            entitiesMap.get(clazz)
                    .remove(id);
            return true;
        } catch (NullPointerException e) {
            LOGGER.error("You have no entity of class {} with id {}", clazz.getName(), id);
            return false;
        }
    }
}
