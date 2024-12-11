package com.ticketing.system.ticketing_backend.controller;

import com.ticketing.system.ticketing_backend.model.Ticket;
import com.ticketing.system.ticketing_backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:5173")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Endpoint to purchase tickets for an event
    @PostMapping("/{id}/purchase")
    public Ticket purchaseTicket(@PathVariable Long id) {
        return ticketService.purchaseTicket(id);
    }

    // Fetch all available (unsold) tickets
    @GetMapping("/available")
    public List<Ticket> getAvailableTickets() {
        return ticketService.getAvailableTickets();
    }
}
