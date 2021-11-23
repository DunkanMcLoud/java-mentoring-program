package ru.epam.task.service.events.listeners;

import ru.epam.task.domain.notification.EventNotification;
import ru.epam.task.domain.notification.RemoveEventNotification;

public class RemovalListener implements ServiceEventListener {

    @Override
    public void update(EventNotification event) {
        if (event instanceof RemoveEventNotification) {
            RemoveEventNotification<?> eventNotification = (RemoveEventNotification<?>) event;
            System.out.printf("%s sequence was removed%n", eventNotification.getData());
        }
    }
}
