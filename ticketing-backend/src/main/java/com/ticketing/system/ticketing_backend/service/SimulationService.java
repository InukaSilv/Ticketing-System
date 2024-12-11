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
    private final AtomicLong ticketIdGenerator = new AtomicLong(1);
    private final SimpMessagingTemplate messagingTemplate;

    public SimulationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public long generateEventId() {
        return eventIdGenerator.getAndIncrement();
    }
    public synchronized void resetTicketIdGenerator() {
        ticketIdGenerator.set(1); // Resets ticket ID generation
    }

    public long generateTicketId() {
        return ticketIdGenerator.getAndIncrement();
    }

    public void saveEvent(Event event) {
        eventStorage.put(event.getId(), event);
    }

    public Event findEventById(Long eventId) {
        return eventStorage.get(eventId);
    }

    public List<Event> listEvents() {
        return new ArrayList<>(eventStorage.values());
    }

    public void addTicketsToEvent(Event event, int count) {
        synchronized (event.getTicketPool()) {
            for (int i = 0; i < count; i++) {
                Ticket newTicket = new Ticket();
                newTicket.setId(generateTicketId()); // Generate a unique ticket ID
                newTicket.setStatus("Available");
                event.getTicketPool().add(newTicket); // Add the ticket to the event's pool
            }
        }
    }

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

        vendorThread.start();
        customerThread.start();
    }

    public void stopSimulation(Long eventId) {
        Thread[] threads = simulationThreads.get(eventId);
        if (threads == null) {
            throw new IllegalStateException("No simulation found or simulation already stopped for this event!");
        }

        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }

        simulationThreads.remove(eventId);
        eventLogs.remove(eventId);

        // Reset ticket pool and ticket ID generator
        Event event = findEventById(eventId);
        if (event != null) {
            synchronized (event.getTicketPool()) {
                event.getTicketPool().clear();
            }
        }
        resetTicketIdGenerator();
    }

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
