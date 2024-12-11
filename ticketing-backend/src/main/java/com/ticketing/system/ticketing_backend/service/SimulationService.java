package com.ticketing.system.ticketing_backend.service;

import com.ticketing.system.ticketing_backend.model.Event;
import com.ticketing.system.ticketing_backend.model.Ticket;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SimulationService {

    private final Map<Long, Event> eventStorage = new ConcurrentHashMap<>();
    private final Map<Long, Thread[]> simulationThreads = new ConcurrentHashMap<>();
    private final Map<Long, Queue<String>> eventLogs = new ConcurrentHashMap<>();
    private final AtomicLong eventIdGenerator = new AtomicLong(1); // ID generator for events
    private final AtomicLong ticketIdGenerator = new AtomicLong(1); // ID generator for tickets
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructor for SimulationService.
     *
     * @param messagingTemplate the messaging template for sending logs to frontend
     */
    public SimulationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Generates a unique event ID.
     *
     * @return a new event ID
     */
    public long generateEventId() {
        return eventIdGenerator.getAndIncrement();
    }

    /**
     * Resets the ticket ID generator.
     */
    public synchronized void resetTicketIdGenerator() {
        ticketIdGenerator.set(1); // Resets ticket ID generation to start from 1
    }

    /**
     * Generates a unique ticket ID.
     *
     * @return a new ticket ID
     */
    public long generateTicketId() {
        return ticketIdGenerator.getAndIncrement();
    }

    /**
     * Saves an event to the event storage.
     *
     * @param event the event to be saved
     */
    public void saveEvent(Event event) {
        eventStorage.put(event.getId(), event);
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param eventId the ID of the event
     * @return the event with the specified ID
     */
    public Event findEventById(Long eventId) {
        return eventStorage.get(eventId);
    }

    /**
     * Lists all the events in storage.
     *
     * @return a list of all events
     */
    public List<Event> listEvents() {
        return new ArrayList<>(eventStorage.values());
    }

    /**
     * Adds a specified number of tickets to the event's ticket pool.
     *
     * @param event the event to add tickets to
     * @param count the number of tickets to add
     */
    public void addTicketsToEvent(Event event, int count) {
        synchronized (event.getTicketPool()) {
            for (int i = 0; i < count; i++) {
                Ticket newTicket = new Ticket();
                newTicket.setId(generateTicketId()); // Generate a unique ticket ID
                newTicket.setStatus("Available"); // Set status to "Available"
                event.getTicketPool().add(newTicket); // Add the ticket to the event's pool
            }
        }
    }

    /**
     * Starts the simulation for an event, creating vendor and customer threads.
     *
     * @param event the event for which the simulation is to be started
     * @throws IllegalStateException if the simulation is already running for this event
     */
    public void startSimulation(Event event) {
        if (simulationThreads.containsKey(event.getId())) {
            throw new IllegalStateException("Simulation is already running for this event!");
        }

        Queue<String> logs = new LinkedBlockingQueue<>();
        eventLogs.put(event.getId(), logs);

        Vendor vendor = new Vendor(event, event.getReleaseRate(), logs, messagingTemplate);
        Customer customer = new Customer(event, event.getBuyingRate(), logs, messagingTemplate);

        Thread vendorThread = new Thread(vendor);
        Thread customerThread = new Thread(customer);

        simulationThreads.put(event.getId(), new Thread[]{vendorThread, customerThread});
        //thread starting
        vendorThread.start();
        customerThread.start();
    }

    /**
     * Stops the simulation for an event, interrupting both vendor and customer threads.
     *
     * @param eventId the ID of the event whose simulation is to be stopped
     * @throws IllegalStateException if no simulation is running for this event
     */
    public void stopSimulation(Long eventId) {
        Thread[] threads = simulationThreads.get(eventId);
        if (threads == null) {
            throw new IllegalStateException("No simulation found or simulation already stopped for this event!");
        }

        // Interrupt vendor and customer threads
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }

        // Remove event from simulation and clear related data
        simulationThreads.remove(eventId);
        eventLogs.remove(eventId);

        // Reset ticket pool and ticket ID generator
        Event event = findEventById(eventId);
        if (event != null) {
            synchronized (event.getTicketPool()) {
                event.getTicketPool().clear(); // Clear ticket pool
            }
        }
        resetTicketIdGenerator(); // Reset ticket ID generation
    }

    /**
     * Retrieves logs for a specific event.
     *
     * @param eventId the ID of the event for which logs are needed
     * @return a queue containing the event's logs
     */
    public Queue<String> getEventLogs(Long eventId) {
        Queue<String> logs = eventLogs.get(eventId);
        if (logs == null || logs.isEmpty()) {
            System.out.println("[DEBUG] No logs found for event ID: " + eventId);
        } else {
            System.out.println("[DEBUG] Logs for event ID " + eventId + ": " + logs);
        }
        return logs;
    }
}
