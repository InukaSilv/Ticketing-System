package com.ticketing.system.ticketing_backend.repository;

import com.ticketing.system.ticketing_backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(String status); // Fetch tickets by status
}
