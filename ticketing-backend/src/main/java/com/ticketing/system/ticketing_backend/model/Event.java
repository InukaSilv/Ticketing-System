package com.ticketing.system.ticketing_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private Long id;

    @JsonProperty("eventName") // Map JSON "eventName" to "name"
    private String name;

    private List<Ticket> ticketPool = new ArrayList<>();
    private List<Ticket> releasedTicketQueue = new ArrayList<>();

    private int maxCapacity;

    @JsonProperty("customerRate") // Map JSON "customerRate" to "buyingRate"
    private int buyingRate;

    @JsonProperty("vendorRate") // Map JSON "vendorRate" to "releaseRate"
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
