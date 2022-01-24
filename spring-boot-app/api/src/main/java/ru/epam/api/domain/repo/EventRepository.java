package ru.epam.api.domain.repo;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.epam.api.domain.entities.Event;

import java.util.UUID;

public interface EventRepository extends R2dbcRepository<Event, UUID> {

    Flux<Event> findAllByTitle(String title);

    Mono<Event> findEventByTitle(String title);
}

