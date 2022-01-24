package ru.epam.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.epam.api.domain.entities.Event;
import ru.epam.api.domain.service.EventService;
import ru.epam.auth.roles.Authorities;


@RestController
@RequiredArgsConstructor
@PreAuthorize(Authorities.USER)
public class EventServiceController {

    private final EventService eventService;

    @GetMapping("/events/{title}")
    @ApiOperation(value = "Get event by title", notes = "Returns event by title")
    @ResponseStatus(HttpStatus.FOUND)
    public Mono<Event> findEventByTitle(@PathVariable String title) {
        return eventService.getEventByTitle(title);
    }

    @GetMapping("/events/all")
    @ApiOperation(value = "List all events", notes = "Returns  all event entities")
    public Flux<Event> listAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping(value = "/events/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create an event", notes = "Creates and stores an event entity")
    public Mono<Event> createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PutMapping(value = "/events/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update event", notes = "Update event by Id")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Event> updateEventById(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }


    @GetMapping(value = "/events/all/{title}")
    @ApiOperation(value = "Get events by title", notes = "Retrieves all events by their titles")
    @ResponseStatus(HttpStatus.FOUND)
    public Flux<Event> getAllEventByTitle(@PathVariable String title) {
        return eventService.getAllEventsByTitle(title);
    }


    @DeleteMapping(value = "/events/delete")
    @ApiOperation(value = "Delete event", notes = "Delete event from Database")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvent(@RequestBody Event event) {
        eventService.deleteEvent(event);
    }

}
