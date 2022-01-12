package ru.epam.spring.domain.dao.impl;

import ru.epam.spring.beans.Dao;
import ru.epam.spring.domain.dao.EntityDao;
import ru.epam.spring.domain.model.Event;
import ru.epam.spring.domain.model.PersistedEntity;
import ru.epam.spring.domain.storage.EntityStorage;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Dao
public class EventDao implements EntityDao<Event> {

    private static AtomicLong ID_GEN = new AtomicLong();
    private final EntityStorage entityStorage;

    public EventDao(EntityStorage entityStorage) {
        this.entityStorage = entityStorage;
    }

    @Override
    public Event store(Event event) {
        event.setId(ID_GEN.addAndGet(1));
        entityStorage.storeEntity(event, Event.class);
        return event;
    }

    @Override
    public Event retrieveById(long id) {
        return (Event) entityStorage.getEntityById(getEntityType(), id);
    }

    @Override
    public Class<Event> getEntityType() {
        return Event.class;
    }

    @Override
    public Event update(Event event) {
        entityStorage.updateEntity(event, Event.class);
        return event;
    }

    @Override
    public boolean delete(long id) {
        return entityStorage.deleteEntityById(Event.class, id);
    }


    public List<Event> getEventsByTitle(String title) {
        return entityStorage.getAllEntitiesOfType(Event.class)
                .stream()
                .filter(Event.class::isInstance)
                .map(Event.class::cast)
                .filter(event -> title.equals(event.getTitle()))
                .collect(Collectors.toList());
    }

    public List<Event> getEventsForDay(Date day) {
        List<PersistedEntity> allEntitiesOfType = entityStorage.getAllEntitiesOfType(Event.class);
        return allEntitiesOfType.stream()
                .filter(o -> getEntityType().isInstance(Event.class))
                .map(o -> (Event) o)
                .filter(event -> day.equals(event.getDate()))
                .collect(Collectors.toList());
    }
}
