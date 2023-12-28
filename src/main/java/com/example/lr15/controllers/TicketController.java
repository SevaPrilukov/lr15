package com.example.lr15.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import com.example.lr15.entities.SpisokTicket;
import com.example.lr15.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TicketController {
    private TicketService ticketService;
    private UserDetailsService userDetailsService;

    @Autowired
    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("")
    public String showTicketsList(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<SpisokTicket> ticketPage = ticketService.getAllTickets(pageable);
        model.addAttribute("tickets", ticketPage.getContent());
        model.addAttribute("ticket", new SpisokTicket());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketPage.getTotalPages());

        List<String> mostCommonRoutes = calculateMostCommonRoutes();
        model.addAttribute("mostCommonRoutes", mostCommonRoutes);



        return "tickets";
    }

    @PostMapping("/tickets/addOrUpdate/add")
    public String addTicket(@ModelAttribute(value = "spisokticket") SpisokTicket spisokticket) {
        ticketService.add(spisokticket);
        return "redirect:/";
    }

    @GetMapping("/tickets/addOrUpdate/add")
    public String test(Model model) {
        Page<SpisokTicket> ticketPage = ticketService.getAllTickets(PageRequest.of(0, 5));
        model.addAttribute("tickets", ticketPage.getContent());
        model.addAttribute("ticket", new SpisokTicket());
        return "addOrUpdate";
    }

    @GetMapping("/tickets/addOrUpdate/edit/{id}")
    public String editTicket(Model model, @PathVariable(value = "id") Integer id) {
        SpisokTicket spisokTicket = ticketService.getById(id);
        model.addAttribute("ticket", spisokTicket);
        return "addOrUpdate";
    }

    @PostMapping("/tickets/addOrUpdate/edit/update")
    public String updateTicket(@ModelAttribute(value = "ticket") SpisokTicket updatedOrganization) {
        SpisokTicket ticket = ticketService.getById(updatedOrganization.getId());
        ticketService.update(ticket, updatedOrganization);
        return "redirect:/";
    }


    @GetMapping("/tickets/show/{id}")
    public String showOneTicket(Model model, @PathVariable(value = "id") Integer id) {
        SpisokTicket spisokTicket = ticketService.getById(id);
        model.addAttribute("ticket", spisokTicket);
        return "ticket-info";
    }

    @GetMapping("/tickets/delete/{id}")
    public String deleteTickets(@PathVariable(value = "id") Integer id) {
        SpisokTicket spisokticket = ticketService.getById(id);
        ticketService.delete(spisokticket);
        return "redirect:/";
    }

    @GetMapping("/tickets/filter")
    public String filterTickets(Model model,
                                @RequestParam(value = "passenger", required = false) String passenger,
                                @RequestParam(value = "route", required = false) String route,
                                @RequestParam(value = "price", required = false) Integer price,
                                @RequestParam(defaultValue = "0") int page) {
        SpisokTicket spisokTicket = new SpisokTicket();

        Pageable pageable = PageRequest.of(page, 5);
        Page<SpisokTicket> ticketPage = ticketService.getAllTickets(passenger, route, price, pageable);

        model.addAttribute("tickets", ticketPage.getContent());
        model.addAttribute("ticket", spisokTicket);
        model.addAttribute("passenger", passenger);
        model.addAttribute("route", route);
        model.addAttribute("price", price);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketPage.getTotalPages());


        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/tickets/filter");
        if (passenger != null && !passenger.isEmpty()) uriBuilder.queryParam("passenger", passenger);
        if (route != null && !route.isEmpty()) uriBuilder.queryParam("route", route);
        if (price != null) uriBuilder.queryParam("price", price);
        model.addAttribute("filterUrl", uriBuilder.build().toString());

        List<String> mostCommonRoutes = calculateMostCommonRoutes();
        model.addAttribute("mostCommonRoutes", mostCommonRoutes);

        return "tickets";
    }

    @PostMapping("/authenticateTheUser")
    public String authenticateUser(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails != null) {
            String storedPassword = userDetails.getPassword();
            if (password.equals(storedPassword)) {
                model.addAttribute("username", username);
                return "redirect:/tickets";
            }
        }
        return "tickets";
    }

    private List<String> calculateMostCommonRoutes() {
        List<SpisokTicket> allTickets = ticketService.getAllTickets();

        // Группировка по адресу и подсчет количества
        Map<String, Long> routesCountMap = allTickets.stream()
                .collect(Collectors.groupingBy(SpisokTicket::getRoute, Collectors.counting()));

        // Сортировка по убыванию количества и выбор топ-3
        List<String> mostCommonRoutes = routesCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return mostCommonRoutes.isEmpty() ? Collections.singletonList("Маршруты отсутствуют") : mostCommonRoutes;
    }
}