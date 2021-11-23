package ru.epam.task.service.events.manager;

import ru.epam.task.service.events.EventType;
import ru.epam.task.service.events.listeners.ServiceEventListener;
import ru.epam.task.domain.notification.EventNotification;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServiceEventManager {

    Map<EventType, List<ServiceEventListener>> listeners = new HashMap<>();
    private static volatile ServiceEventManager INSTANCE;


    private ServiceEventManager(EventType... events) {
        for (EventType eventType : events) {
            this.listeners.put(eventType, new LinkedList<>());
        }
    }

    public static ServiceEventManager getInstance() {
        ServiceEventManager localManager = INSTANCE;
        if (localManager == null) {
            synchronized (ServiceEventManager.class) {
                localManager = INSTANCE;
                if (localManager == null) {
                    INSTANCE = localManager = new ServiceEventManager(EventType.REMOVE);
                }
            }
        }
        return localManager;
    }


    public void subscribe(EventType eventType, ServiceEventListener listener) {
        List<ServiceEventListener> clients = listeners.get(eventType);
        clients.add(listener);
    }

    public void unsubscribe(EventType eventType, ServiceEventListener listener) {
        List<ServiceEventListener> clients = listeners.get(eventType);
        clients.remove(listener);
    }

    public void notify(EventNotification eventNotification) {
        List<ServiceEventListener> clients = listeners.get(eventNotification.getType());
        for (ServiceEventListener listener : clients) {
            listener.update(eventNotification);
        }
    }
}
