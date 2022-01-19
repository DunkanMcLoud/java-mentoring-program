package ru.epam.spring.beans.initor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.epam.spring.domain.model.Ticket;
import ru.epam.spring.domain.model.impl.EventImpl;
import ru.epam.spring.domain.model.impl.UserImpl;
import ru.epam.spring.domain.services.facade.BookingFacade;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;

public class XmlStorageFiller {

    private static Logger LOGGER = LoggerFactory.getLogger(XmlStorageFiller.class);

    @Autowired
    private BookingFacade bookingFacade;

    public boolean preloadData() {
        try (InputStream inputStream = getClass().getResourceAsStream("/bootstrap-data.xml")) {
            Path tempFile = Files.createTempFile("temp", "xml");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            File xmlFile = tempFile.toFile();
            Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
            initStorageFromXml(xmlDocument);
            return true;
        } catch (IOException exception) {
            LOGGER.error("File not found");
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void initStorageFromXml(Document xmlFile) {
        Element root = (Element) xmlFile.getElementsByTagName(XmlTags.ROOT.getName()).item(0);
        initUsers(root);
        initEvents(root);
        initTickets(root);
    }

    private void initTickets(Element xmlFile) {
        Element ticketsNode = getElement(xmlFile, XmlTags.TICKETS.getName());
        NodeList ticketsList = ticketsNode.getElementsByTagName(XmlTags.TICKET.getName());
        for (int i = 0; i < ticketsList.getLength(); i++) {
            initTicket((Element) ticketsList.item(i));
        }
    }

    private void initTicket(Element item) {
        bookingFacade.bookTicket(
                Long.parseLong(item.getElementsByTagName(XmlTags.USER_ID.getName()).item(0).getTextContent()),
                Long.parseLong(item.getElementsByTagName(XmlTags.EVENT_ID.getName()).item(0).getTextContent()),
                Integer.parseInt(item.getElementsByTagName(XmlTags.PLACE.getName()).item(0).getTextContent()),
                Ticket.Category.valueOf(item.getElementsByTagName(XmlTags.CATEGORY.getName()).item(0).getTextContent())
        );
    }

    private void initEvents(Element xmlFile) {
        Element eventsNode = getElement(xmlFile, XmlTags.EVENTS.getName());
        NodeList eventsList = eventsNode.getElementsByTagName(XmlTags.EVENT.getName());
        for (int i = 0; i < eventsList.getLength(); i++) {
            EventImpl event = mapEvent((Element) eventsList.item(i));
            bookingFacade.createEvent(event);
        }
    }

    private EventImpl mapEvent(Element item) {
        return new EventImpl(
                item.getElementsByTagName(XmlTags.TITLE.getName()).item(0).getTextContent(),
                Date.valueOf(item.getElementsByTagName(XmlTags.DATE.getName()).item(0).getTextContent())
        );
    }

    private void initUsers(Element xmlFile) {
        Element usersNode = getElement(xmlFile, XmlTags.USERS.getName());
        NodeList userList = usersNode.getElementsByTagName(XmlTags.USER.getName());
        for (int i = 0; i < userList.getLength(); i++) {
            UserImpl user = mapUser((Element) userList.item(i));
            bookingFacade.createUser(user);
        }
    }

    private UserImpl mapUser(Element item) {
        return new UserImpl(
                item.getElementsByTagName(XmlTags.NAME.getName()).item(0).getTextContent(),
                item.getElementsByTagName(XmlTags.MAIL.getName()).item(0).getTextContent());
    }


    private static Element getElement(Element elementSource, String tagName) {
        return (Element) elementSource.getElementsByTagName(tagName).item(0);
    }

    private static Element getElement(Document elementSource, String tagName) {
        return (Element) elementSource.getElementsByTagName(tagName).item(0);
    }
}
