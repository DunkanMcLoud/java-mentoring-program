package ru.epam.task.service.events.listeners;

import ru.epam.task.domain.notification.EventNotification;

import java.util.EventListener;

public interface ServiceEventListener extends EventListener {

    void update(EventNotification event);

}
