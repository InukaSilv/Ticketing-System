package com.ticketing.system.ticketing_backend.service;

import com.ticketing.system.ticketing_backend.model.Ticket;
import com.ticketing.system.ticketing_backend.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    /**
     * Constructor for TicketService.
     *
     * @param ticketRepository the TicketRepository to interact with the ticket database
     */
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Purchase a ticket by marking its status as "Sold".
     *
     * @param id the ID of the ticket to purchase
     * @return the updated ticket after marking it as sold
     * @throws IllegalArgumentException if the ticket is not found
     * @throws IllegalStateException if the ticket is already sold
     */
    public Ticket purchaseTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        if ("Sold".equalsIgnoreCase(ticket.getStatus())) {
            throw new IllegalStateException("Ticket is already sold.");
        }
        ticket.setStatus("Sold"); // Mark ticket as sold
        return ticketRepository.save(ticket);
    }

    /**
     * Retrieve all tickets with status "Available".
     *
     * @return a list of available tickets
     */
    public List<Ticket> getAvailableTickets() {
        return ticketRepository.findByStatus("Available");
    }
}
