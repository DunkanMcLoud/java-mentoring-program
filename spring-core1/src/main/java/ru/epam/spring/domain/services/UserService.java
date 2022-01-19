package ru.epam.spring.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.epam.spring.domain.dao.impl.EventDao;
import ru.epam.spring.domain.dao.impl.TicketDao;
import ru.epam.spring.domain.dao.impl.UserDao;
import ru.epam.spring.domain.model.Event;
import ru.epam.spring.domain.model.Ticket;
import ru.epam.spring.domain.model.User;
import ru.epam.spring.domain.model.impl.TicketImpl;
import ru.epam.spring.util.Utils;

import java.util.List;

public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private TicketDao ticketDao;
    @Autowired
    private EventDao eventDao;

    public User getUserById(long userId) {
        return userDao.retrieveById(userId);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public List<User> getUsersByName(String name) {
        return userDao.getUsersByName(name);
    }

    public User createUser(User user) {
        return userDao.store(user);
    }

    public User updateUser(User user) {
        return userDao.update(user);
    }

    public boolean deleteUserById(long userId) {
        return userDao.delete(userId);
    }

    public Ticket bookTicketForUser(long userId, long eventId, int place, Ticket.Category category) {
        User user = userDao.retrieveById(userId);
        Event event = eventDao.retrieveById(eventId);
        if (user != null && event != null) {
            return ticketDao.store(new TicketImpl(eventId, user.getId(), place, category));
        } else {
            throw new UnsupportedOperationException(
                    String.format("Could not book a ticket.\nUser existence: %s \n Event existence: %s", (user != null), (event != null))
            );
        }
    }

    public List<Ticket> getBookedTicketsForUser(User user) {
        User user1 = userDao.retrieveById(user.getId());
        if (user1 != null) {
            return ticketDao.getAllTicketsForUser(user1.getId());
        } else {
            throw new UnsupportedOperationException(
                    String.format("User with id %d does not exists", user.getId())
            );
        }
    }
}
