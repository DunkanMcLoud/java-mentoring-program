package ru.epam.spring.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.epam.spring.config.RootConfig;
import ru.epam.spring.config.WebConfig;
import ru.epam.spring.domain.storage.EntityStorage;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class, WebConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EntityStorage entityStorage;

    private MockMvc mockMvc;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        mockMvc.getDispatcherServlet().setThrowExceptionIfNoHandlerFound(true);
    }

    @Test
    public void createUser_should_create() throws Exception {
        mockMvc.perform(post("/createUser?name=Johny&email=johny@mail"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", hasProperty("id", is(1L))))
                .andExpect(model().attribute("user", hasProperty("name", is("Johny"))))
                .andExpect(model().attribute("user", hasProperty("email", is("johny@mail"))))
                .andExpect(view().name("user"));

        mockMvc.perform(post("/createUser?name=Billy&email=billy@mail&some=unknown"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", hasProperty("id", is(2L))))
                .andExpect(model().attribute("user", hasProperty("name", is("Billy"))))
                .andExpect(model().attribute("user", hasProperty("email", is("billy@mail"))))
                .andExpect(view().name("user"));
    }

    @Test
    public void createUser_shouldNot_create() throws Exception {
        mockMvc.perform(post("/createUser?name=Johny"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/createUser?email=johny@mail"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("//createUser?some=johny@mail"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldDisplayTickets() throws Exception {
        mockMvc.perform(post("/preloadData"));
        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
               /* .andExpect(
                        model().attribute("tickets.ticket", allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("eventId", is(1L)),
                                hasProperty("place", is(3)),
                                hasProperty("category", is("PREMIUM")))))*/
                .andExpect(view().name("tickets"));
    }

    @Test
    public void shouldDisplayErrorPage() throws Exception {
        mockMvc.perform(get("/unknownPath"))
                .andExpect(status().is5xxServerError())
                .andExpect(view().name("error"));
    }

}
