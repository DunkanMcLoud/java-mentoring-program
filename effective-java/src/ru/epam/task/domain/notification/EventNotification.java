package ru.epam.task.domain.notification;

import ru.epam.task.service.events.EventType;

public interface EventNotification {

    EventType getType();

}
