package ru.epam.task.domain.notification;

import ru.epam.task.service.events.EventType;

public final class RemoveEventNotification<D> implements EventNotification {

    private static final EventType type = EventType.REMOVE;
    private final D data;

    public D getData() {
        return data;
    }

    private RemoveEventNotification(D data) {
        this.data = data;
    }

    public static<D> RemoveEventNotification<D> of(D data) {
        return new RemoveEventNotification<>(data);
    }

    @Override
    public EventType getType() {
        return type;
    }
}
