package ru.epam.spring.beans.initor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.epam.spring.config.RootConfig;
import ru.epam.spring.config.WebConfig;
import ru.epam.spring.domain.model.Event;
import ru.epam.spring.domain.model.PersistedEntity;
import ru.epam.spring.domain.model.Ticket;
import ru.epam.spring.domain.model.User;
import ru.epam.spring.domain.storage.EntityStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class,WebConfig.class})
public class BootStrapDataTest {

    @Autowired
    private EntityStorage entityStorage;


    @Autowired
    private XmlStorageFiller storagePopulator;


    @Test
    public void checkParsingIsDone() {
        //when
        storagePopulator.preloadData();

        List<PersistedEntity> users = entityStorage.getAllEntitiesOfType(User.class);
        List<PersistedEntity> events = entityStorage.getAllEntitiesOfType(Event.class);
        List<PersistedEntity> tickets = entityStorage.getAllEntitiesOfType(Ticket.class);
        assertThat(users.size()).isEqualTo(3);
        assertThat(events.size()).isEqualTo(3);
        assertThat(tickets.size()).isEqualTo(3);

        //then
        users.forEach(user->{
            assertThat(user).hasNoNullFieldsOrProperties();
        });

        events.forEach(event->{
            assertThat(event).hasNoNullFieldsOrProperties();
        });

        tickets.forEach(ticket->{
            assertThat(ticket).hasNoNullFieldsOrProperties();
        });
    }
}
