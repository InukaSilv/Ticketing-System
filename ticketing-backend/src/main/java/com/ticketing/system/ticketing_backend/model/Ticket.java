package com.ticketing.system.ticketing_backend.model;

import jakarta.persistence.*;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate IDs
    private Long id;

    @Column(nullable = false) // Mark as non-nullable
    private String status;

    // Default constructor for JPA
    public Ticket() {}

    // Constructor
    public Ticket(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!status.equals("Available") && !status.equals("Sold") && !status.equals("Purchased")) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        this.status = status;
    }

}
