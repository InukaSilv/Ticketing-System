package com.ticketing.system.ticketing_backend.model;

import jakarta.persistence.*;

/**
 * Entity representing a ticket in the ticketing system.
 */
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate IDs
    private Long id;

    @Column(nullable = false) // Mark as non-nullable
    private String status;

    // Default constructor for JPA
    public Ticket() {}

    /**
     * Constructor for creating a ticket with specified ID and status.
     * @param id the ticket ID
     * @param status the ticket status
     */
    public Ticket(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    // Getters and Setters

    /**
     * Get the ID of the ticket.
     * @return the ticket ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the ticket.
     * @param id the ticket ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the status of the ticket.
     * @return the ticket status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status of the ticket. Valid statuses are "Available", "Sold", and "Purchased".
     * @param status the ticket status
     * @throws IllegalArgumentException if the status is invalid
     */
    public void setStatus(String status) {
        if (!status.equals("Available") && !status.equals("Sold") && !status.equals("Purchased")) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        this.status = status;
    }
}
