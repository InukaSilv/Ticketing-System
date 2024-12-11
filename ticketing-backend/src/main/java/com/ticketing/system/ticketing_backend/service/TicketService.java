package com.ticketing.system.ticketing_backend.service;

import com.ticketing.system.ticketing_backend.model.Ticket;
import com.ticketing.system.ticketing_backend.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // Purchase a ticket by marking its status as "Sold"
    public Ticket purchaseTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        if ("Sold".equalsIgnoreCase(ticket.getStatus())) {
            throw new IllegalStateException("Ticket is already sold.");
        }
        ticket.setStatus("Sold"); // Mark ticket as sold
        return ticketRepository.save(ticket);
    }

    // Retrieve all tickets with status "Available"
    public List<Ticket> getAvailableTickets() {
        return ticketRepository.findByStatus("Available");
    }
}
