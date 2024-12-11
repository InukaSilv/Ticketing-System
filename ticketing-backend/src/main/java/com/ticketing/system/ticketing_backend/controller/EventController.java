package com.ticketing.system.ticketing_backend.controller;

import com.ticketing.system.ticketing_backend.model.Event;
import com.ticketing.system.ticketing_backend.model.Ticket;
import com.ticketing.system.ticketing_backend.service.SimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing events and tickets.
 * Provides endpoints to create events, add tickets, and retrieve tickets.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from frontend
@RequestMapping("/api/event")
public class EventController {
    private final SimulationService simulationService;

    /**
     * Constructor to inject SimulationService.
     * @param simulationService service to manage event data
     */
    public EventController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    /**
     * Creates a new event and assigns it a unique ID.
     * @param newEvent the event to create
     * @return the created event
     */
    @PostMapping("/create")
    public Event createEvent(@RequestBody Event newEvent) {
        newEvent.setId(simulationService.generateEventId()); // Generate event ID
        simulationService.saveEvent(newEvent); // Save event
        return newEvent;
    }

    /**
     * Retrieves all events.
     * @return a list of all events
     */
    @GetMapping
    public List<Event> getAllEvents() {
        return simulationService.listEvents(); // Get events list
    }

    /**
     * Adds a specified number of tickets to an event.
     * @param eventId the ID of the event to add tickets to
     * @param count the number of tickets to add
     * @return a response indicating the success or failure of the operation
     */
    @PostMapping("/{eventId}/tickets/add")
    public ResponseEntity<String> addTickets(@PathVariable Long eventId, @RequestParam int count) {
        Event event = simulationService.findEventById(eventId); // Get event by ID
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found!");
        }

        synchronized (event.getTicketPool()) {
            int currentCount = event.getTicketPool().size();
            if (currentCount + count > event.getMaxCapacity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cannot add tickets. Max capacity exceeded!");
            }

            // Add new tickets
            for (int i = 0; i < count; i++) {
                Ticket newTicket = new Ticket();
                newTicket.setId(simulationService.generateTicketId()); // Generate ticket ID
                newTicket.setStatus("Available");
                event.getTicketPool().add(newTicket);
            }

            event.getTicketPool().notifyAll(); // Notify threads
        }

        return ResponseEntity.ok(count + " tickets added successfully!");
    }

    /**
     * Retrieves all tickets for a specific event.
     * @param eventId the ID of the event to fetch tickets for
     * @return a list of ticket details
     */
    @GetMapping("/{eventId}/tickets")
    public ResponseEntity<List<String>> getTickets(@PathVariable Long eventId) {
        Event event = simulationService.findEventById(eventId); // Get event by ID
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of("Event not found!"));
        }

        // List ticket details
        List<String> ticketDetails = event.getTicketPool().stream()
                .map(ticket -> "Ticket ID: " + ticket.getId() + ", Status: " + ticket.getStatus())
                .toList();

        return ResponseEntity.ok(ticketDetails);
    }
}
