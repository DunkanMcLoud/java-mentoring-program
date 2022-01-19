package ru.epam.spring.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.epam.spring.domain.dao.impl.EventDao;
import ru.epam.spring.domain.model.Event;
import ru.epam.spring.util.Utils;

import java.util.Date;
import java.util.List;

public class EventService {

    @Autowired
    private EventDao eventDao;

    public Event getEventById(long eventId) {
        return eventDao.retrieveById(eventId);
    }

    public List<Event> getEventsByTitle(String title) {
        return eventDao.getEventsByTitle(title);
    }

    public Event createEvent(Event event) {
        return eventDao.store(event);
    }

    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        List<Event> eventsForDay = eventDao.getEventsForDay(day);
        return Utils.getElementsForPage(eventsForDay, pageSize, pageNum);
    }

    public Event updateEvent(Event event) {
        return eventDao.update(event);
    }

    public boolean deleteEvent(long eventId) {
        return eventDao.delete(eventId);
    }
}
