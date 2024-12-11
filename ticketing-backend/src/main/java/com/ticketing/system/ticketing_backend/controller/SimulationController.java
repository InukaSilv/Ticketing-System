package com.ticketing.system.ticketing_backend.controller;

import com.ticketing.system.ticketing_backend.model.Event;
import com.ticketing.system.ticketing_backend.service.SimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Controller for managing simulations of events.
 * Provides endpoints to start, stop, and retrieve logs for simulations.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from frontend
@RequestMapping("/api/simulation")
public class SimulationController {
    private final SimulationService simulationService;

    /**
     * Constructor to inject SimulationService.
     * @param simulationService service to manage event simulations
     */
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    /**
     * Starts the simulation for a specific event.
     * @param eventId the ID of the event to start the simulation for
     * @return a response indicating the success or failure of the operation
     */
    @PostMapping("/start/{eventId}")
    public ResponseEntity<String> startSimulation(@PathVariable Long eventId) {
        Event event = simulationService.findEventById(eventId); // Get event by ID
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found!");
        }
        simulationService.startSimulation(event); // Start the simulation
        return ResponseEntity.ok("Simulation started for event: " + event.getName());
    }

    /**
     * Stops the simulation for a specific event.
     * @param eventId the ID of the event to stop the simulation for
     * @return a response indicating the success or failure of the operation
     */
    @PostMapping("/stop/{eventId}")
    public ResponseEntity<String> stopSimulation(@PathVariable Long eventId) {
        try {
            simulationService.stopSimulation(eventId); // Stop the simulation
            return ResponseEntity.ok("Simulation stopped for event: " + eventId);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Retrieves the logs for a specific event.
     * @param eventId the ID of the event to retrieve logs for
     * @return a list of event logs
     */
    @GetMapping("/logs/{eventId}")
    public ResponseEntity<List<String>> getEventLogs(@PathVariable Long eventId) {
        Queue<String> logs = simulationService.getEventLogs(eventId); // Get logs for event
        if (logs == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of("No logs available."));
        }
        return ResponseEntity.ok(new ArrayList<>(logs)); // Convert Queue to List
    }
}
