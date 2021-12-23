package ru.epam.spring;

import org.assertj.core.api.JUnitSoftAssertions;


import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.epam.spring.domain.model.Event;
import ru.epam.spring.domain.model.Ticket;
import ru.epam.spring.domain.model.User;
import ru.epam.spring.domain.model.impl.EventImpl;
import ru.epam.spring.domain.model.impl.UserImpl;
import ru.epam.spring.domain.services.facade.BookingFacade;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(value = "classpath:application-context.xml")
class AppIntegrationTest {

    private static final User john = new UserImpl("John", "te@mail");
    private static final Event event1 = new EventImpl("event1", Date.from(Instant.now().minus(10, ChronoUnit.DAYS)));
    private static final Event event2 = new EventImpl("event2", Date.from(Instant.now().minus(5, ChronoUnit.DAYS)));
    private static final Event event3 = new EventImpl("event3", Date.from(Instant.now().minus(2, ChronoUnit.DAYS)));
    private static final Event event4 = new EventImpl("event4", Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
    private static final Event event5 = new EventImpl("event5", Date.from(Instant.now()));

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Autowired
    private BookingFacade bookingFacade;

    @Test
    public void workflow() {
        //when
        User user = bookingFacade.createUser(john);

        Event firstEvent = bookingFacade.createEvent(event1);
        Event secondEvent = bookingFacade.createEvent(event2);
        Event thirdEvent = bookingFacade.createEvent(event3);
        Event fourthEvent = bookingFacade.createEvent(event4);
        Event fifthEvent = bookingFacade.createEvent(event5);

        //book some tickets
        Ticket ticket1 = bookingFacade.bookTicket(user.getId(), firstEvent.getId(), 7, Ticket.Category.PREMIUM);
        Ticket ticket2 = bookingFacade.bookTicket(user.getId(), secondEvent.getId(), 4, Ticket.Category.STANDARD);
        Ticket ticket3 = bookingFacade.bookTicket(user.getId(), thirdEvent.getId(), 3, Ticket.Category.BAR);
        Ticket ticket4 = bookingFacade.bookTicket(user.getId(), fourthEvent.getId(), 2, Ticket.Category.PREMIUM);
        Ticket ticket5 = bookingFacade.bookTicket(user.getId(), fifthEvent.getId(), 10, Ticket.Category.PREMIUM);

        //then
        List<Ticket> bookedTickets1 = bookingFacade.getBookedTickets(user, 2, 2);
        List<Ticket> bookedTickets2 = bookingFacade.getBookedTickets(secondEvent, 2, 2);

        softly.assertThat(bookedTickets1).containsSequence(ticket4, ticket3);
        softly.assertThat(bookedTickets2).containsOnly(ticket2);

        //cancel tickets
        bookingFacade.cancelTicket(ticket3.getId());
        bookingFacade.cancelTicket(ticket4.getId());
        List<Ticket> bookedTickets1AfterDelete = bookingFacade.getBookedTickets(user, 2, 2);

        softly.assertThat(bookedTickets1AfterDelete).isEmpty();

        //delete event and check it disappeared
        boolean deleteEvent = bookingFacade.deleteEvent(firstEvent.getId());
        assertTrue(deleteEvent);

        Event eventById = bookingFacade.getEventById(firstEvent.getId());
        assertThat(eventById).isNull();
    }


}

