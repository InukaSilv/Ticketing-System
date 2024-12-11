package com.ticketing.system.ticketing_backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events") // Table name in the database
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("eventName") // Map JSON "eventName" to "name"
    private String name;

    @Transient
    private List<Ticket> ticketPool = new ArrayList<>();

    @Transient
    private List<Ticket> releasedTicketQueue = new ArrayList<>();

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    @JsonProperty("customerRate")
    private int buyingRate;

    @Column(nullable = false)
    @JsonProperty("vendorRate")
    private int releaseRate;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ticket> getTicketPool() {
        return ticketPool;
    }

    public void setTicketPool(List<Ticket> ticketPool) {
        this.ticketPool = ticketPool;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getBuyingRate() {
        return buyingRate;
    }

    public void setBuyingRate(int buyingRate) {
        this.buyingRate = buyingRate;
    }

    public int getReleaseRate() {
        return releaseRate;
    }

    public void setReleaseRate(int releaseRate) {
        this.releaseRate = releaseRate;
    }

    public List<Ticket> getReleasedTicketQueue() {
        return releasedTicketQueue;
    }

    public void setReleasedTicketQueue(List<Ticket> releasedTicketQueue) {
        this.releasedTicketQueue = releasedTicketQueue;
    }
}
