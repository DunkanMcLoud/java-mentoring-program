package ru.epam.spring.beans.initor;

public enum XmlTags {
    ROOT("root"),
    USERS("Users"),
    USER("user"),
    NAME("name"),
    MAIL("mail"),
    EVENTS("Events"),
    EVENT("event"),
    TITLE("title"),
    DATE("date"),
    TICKETS("Tickets"),
    TICKET("ticket"),
    USER_ID("userId"),
    PLACE("place"),
    EVENT_ID("eventId"),
    CATEGORY("category");

    private final String name;

    XmlTags(String tagName) {
        this.name = tagName;
    }

    protected String getName() {
        return this.name;
    }


}
