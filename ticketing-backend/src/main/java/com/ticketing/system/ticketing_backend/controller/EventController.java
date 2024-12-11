package com.ticketing.system.ticketing_backend.controller;

import com.ticketing.system.ticketing_backend.model.Event;
import com.ticketing.system.ticketing_backend.model.Ticket;
import com.ticketing.system.ticketing_backend.service.SimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/event")
public class EventController {
    private final SimulationService simulationService;

    // Inject SimulationService in the constructor
    public EventController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    // Create a new event
    @PostMapping("/create")
    public Event createEvent(@RequestBody Event newEvent) {
        newEvent.setId(simulationService.generateEventId()); // Use centralized ID generator
        simulationService.saveEvent(newEvent); // Save directly in SimulationService storage
        return newEvent;
    }

    // Get all events
    @GetMapping
    public List<Event> getAllEvents() {
        return simulationService.listEvents(); // Fetch from SimulationService storage
    }

    @PostMapping("/{eventId}/tickets/add")
    public ResponseEntity<String> addTickets(@PathVariable Long eventId, @RequestParam int count) {
        Event event = simulationService.findEventById(eventId); // Fetch event details
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found!");
        }

        synchronized (event.getTicketPool()) {
            int currentCount = event.getTicketPool().size();
            if (currentCount + count > event.getMaxCapacity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cannot add tickets. Max capacity exceeded!");
            }

            // Add tickets with proper IDs
            for (int i = 0; i < count; i++) {
                Ticket newTicket = new Ticket();
                newTicket.setId(simulationService.generateTicketId());
                newTicket.setStatus("Available");
                event.getTicketPool().add(newTicket);
            }

            // Notify waiting Vendor/Customer threads
            event.getTicketPool().notifyAll();
        }

        return ResponseEntity.ok(count + " tickets added successfully!");
    }

    // Get all tickets for an event
    @GetMapping("/{eventId}/tickets")
    public ResponseEntity<List<String>> getTickets(@PathVariable Long eventId) {
        Event event = simulationService.findEventById(eventId); // Fetch from centralized storage
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of("Event not found!"));
        }

        // Create a list of ticket details from the ticket pool
        List<String> ticketDetails = event.getTicketPool().stream()
                .map(ticket -> "Ticket ID: " + ticket.getId() + ", Status: " + ticket.getStatus())
                .toList();

        return ResponseEntity.ok(ticketDetails);
    }
}
