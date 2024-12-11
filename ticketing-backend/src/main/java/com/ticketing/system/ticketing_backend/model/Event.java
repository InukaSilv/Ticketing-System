package com.ticketing.system.ticketing_backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an event in the ticketing system.
 */
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

    /**
     * Get the ID of the event.
     * @return the event ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the event.
     * @param id the event ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the name of the event.
     * @return the event name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the event.
     * @param name the event name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the ticket pool for the event.
     * @return the list of tickets in the pool
     */
    public List<Ticket> getTicketPool() {
        return ticketPool;
    }

    /**
     * Set the ticket pool for the event.
     * @param ticketPool the list of tickets in the pool
     */
    public void setTicketPool(List<Ticket> ticketPool) {
        this.ticketPool = ticketPool;
    }

    /**
     * Get the maximum capacity of the event.
     * @return the maximum ticket capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Set the maximum capacity of the event.
     * @param maxCapacity the maximum ticket capacity
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * Get the buying rate for the event.
     * @return the customer buying rate
     */
    public int getBuyingRate() {
        return buyingRate;
    }

    /**
     * Set the buying rate for the event.
     * @param buyingRate the customer buying rate
     */
    public void setBuyingRate(int buyingRate) {
        this.buyingRate = buyingRate;
    }

    /**
     * Get the release rate for the event.
     * @return the vendor release rate
     */
    public int getReleaseRate() {
        return releaseRate;
    }

    /**
     * Set the release rate for the event.
     * @param releaseRate the vendor release rate
     */
    public void setReleaseRate(int releaseRate) {
        this.releaseRate = releaseRate;
    }

    /**
     * Get the released ticket queue for the event.
     * @return the list of released tickets
     */
    public List<Ticket> getReleasedTicketQueue() {
        return releasedTicketQueue;
    }

    /**
     * Set the released ticket queue for the event.
     * @param releasedTicketQueue the list of released tickets
     */
    public void setReleasedTicketQueue(List<Ticket> releasedTicketQueue) {
        this.releasedTicketQueue = releasedTicketQueue;
    }
}
