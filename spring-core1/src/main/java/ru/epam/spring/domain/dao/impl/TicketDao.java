package ru.epam.spring.domain.dao.impl;

import ru.epam.spring.beans.Dao;
import ru.epam.spring.domain.dao.EntityDao;
import ru.epam.spring.domain.model.Event;
import ru.epam.spring.domain.model.Ticket;
import ru.epam.spring.domain.storage.EntityStorage;

import java.util.List;
import java.util.stream.Collectors;

@Dao
public class TicketDao implements EntityDao<Ticket> {

    private static long ID_GEN = 0L;
    private final EntityStorage entityStorage;

    public TicketDao(EntityStorage entityStorage) {
        this.entityStorage = entityStorage;
    }

    @Override
    public Ticket store(Ticket entity) {
        entity.setId(++ID_GEN);
        entityStorage.storeEntity(entity,Ticket.class);
        return entity;
    }

    @Override
    public Ticket retrieveById(long id) {
        Object entityById = entityStorage.getEntityById(Ticket.class, id);
        return (Ticket) entityById;
    }

    @Override
    public Class<Ticket> getEntityType() {
        return Ticket.class;
    }

    @Override
    public Ticket update(Ticket entity) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return entityStorage.deleteEntityById(Ticket.class, id);
    }

    public List<Ticket> getAllTicketsForUser(long userId) {
        return entityStorage.getAllEntitiesOfType(Ticket.class)
                .stream()
                .filter(Ticket.class::isInstance)
                .map(Ticket.class::cast)
                .filter(ticket -> userId == ticket.getUserId())
                .collect(Collectors.toList());
    }

    public List<Ticket> getAllTicketsForEvent(Event event) {
        return entityStorage.getAllEntitiesOfType(Ticket.class)
                .stream()
                .filter(Ticket.class::isInstance)
                .map(Ticket.class::cast)
                .filter(ticket -> event.getId() == ticket.getEventId())
                .collect(Collectors.toList());
    }
}
