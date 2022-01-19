package ru.epam.spring.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.epam.spring.domain.dao.impl.TicketDao;
import ru.epam.spring.domain.model.Event;
import ru.epam.spring.domain.model.Ticket;

import java.util.List;

public class TicketService {

    @Autowired
    private TicketDao ticketDao;


    public List<Ticket> getTicketsForEvent(Event event) {
        return ticketDao.getAllTicketsForEvent(event);
    }

    public boolean cancelTicket(long ticketId) {
        return ticketDao.delete(ticketId);
    }
}
