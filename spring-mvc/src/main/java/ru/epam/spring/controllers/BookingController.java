package ru.epam.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.epam.spring.beans.initor.XmlStorageFiller;
import ru.epam.spring.domain.model.Ticket;
import ru.epam.spring.domain.model.User;
import ru.epam.spring.domain.model.impl.UserImpl;
import ru.epam.spring.domain.services.facade.BookingFacade;

import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private BookingFacade bookingFacade;
    @Autowired
    private XmlStorageFiller storageFiller;


    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "Hello Spring MVC 5!");
        return "index";
    }


    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/tickets")
    public String showTickets(Model model) {
        List<Ticket> allBookedTickets = bookingFacade.getAllBookedTickets();
        model.addAttribute("tickets", allBookedTickets);
        return "tickets";
    }


    @PostMapping(value = "/preloadData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean preloadData(Model model) {
        return storageFiller.preloadData();
    }

    @PostMapping("/createUser")
    public String createUser(@RequestParam String name, @RequestParam String email, Model model) {
        User user = bookingFacade.createUser(new UserImpl(name, email));
        model.addAttribute("user", user);
        return "user";
    }


}
