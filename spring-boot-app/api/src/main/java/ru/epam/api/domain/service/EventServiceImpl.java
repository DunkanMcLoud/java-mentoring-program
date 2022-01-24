package ru.epam.api.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.epam.api.domain.entities.Event;
import ru.epam.api.domain.repo.EventRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public Mono<Event> createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Mono<Event> updateEvent(Event event) {
        return eventRepository.save(event);
    }

    public Mono<Event> getEvent(UUID id) {
        return eventRepository.findById(id);
    }

    public Flux<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Flux<Event> getAllEventsByTitle(String title) {
        return eventRepository.findAllByTitle(title);
    }

    public Mono<Event> getEventByTitle(String title) {
        return eventRepository.findEventByTitle(title);
    }

    public Mono<Void> deleteEvent(Event event) {
        return eventRepository.delete(event);
    }
}
