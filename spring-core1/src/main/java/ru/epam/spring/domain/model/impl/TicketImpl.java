package ru.epam.spring.domain.model.impl;

import ru.epam.spring.domain.model.Ticket;

public class TicketImpl implements Ticket {

    private long id;
    private long eventId;
    private long userId;
    private int place;
    private Category category;

    public TicketImpl(long eventId, long userId, int place, Category category) {
        this.eventId = eventId;
        this.userId = userId;
        this.place = place;
        this.category = category;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getEventId() {
        return this.eventId;
    }

    @Override
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public long getUserId() {
        return this.userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public Category getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int getPlace() {
        return place;
    }

    @Override
    public void setPlace(int place) {
        this.place = place;
    }
}
