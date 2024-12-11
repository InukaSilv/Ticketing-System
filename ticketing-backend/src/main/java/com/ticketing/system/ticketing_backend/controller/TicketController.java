package com.ticketing.system.ticketing_backend.controller;

import com.ticketing.system.ticketing_backend.model.Ticket;
import com.ticketing.system.ticketing_backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing tickets.
 * Provides endpoints to purchase tickets and fetch available tickets.
 */
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from frontend
public class TicketController {
    private final TicketService ticketService;

    /**
     * Constructor to inject TicketService.
     * @param ticketService service to manage ticket operations
     */
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Purchases a ticket for a given event.
     * @param id the ID of the ticket to purchase
     * @return the purchased ticket
     */
    @PostMapping("/{id}/purchase")
    public Ticket purchaseTicket(@PathVariable Long id) {
        return ticketService.purchaseTicket(id); // Process ticket purchase
    }

    /**
     * Retrieves all available (unsold) tickets.
     * @return a list of available tickets
     */
    @GetMapping("/available")
    public List<Ticket> getAvailableTickets() {
        return ticketService.getAvailableTickets(); // Fetch available tickets
    }
}
