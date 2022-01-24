package ru.epam.api.domain.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.epam.api.domain.entities.Event;

import java.util.UUID;

public interface EventService {

    Mono<Event> createEvent(Event event);

    Mono<Event> updateEvent(Event event);

    Mono<Event> getEvent(UUID id);

    Flux<Event> getAllEvents();

    Flux<Event> getAllEventsByTitle(String title);

    Mono<Event> getEventByTitle(String title);

    Mono<Void> deleteEvent(Event event);

}
