package com.ticketing.system.ticketing_backend.controller;

import com.ticketing.system.ticketing_backend.model.Event;
import com.ticketing.system.ticketing_backend.service.SimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/simulation")
public class SimulationController {
    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/start/{eventId}")
    public ResponseEntity<String> startSimulation(@PathVariable Long eventId) {
        Event event = simulationService.findEventById(eventId);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found!");
        }
        simulationService.startSimulation(event);
        return ResponseEntity.ok("Simulation started for event: " + event.getName());
    }

    @PostMapping("/stop/{eventId}")
    public ResponseEntity<String> stopSimulation(@PathVariable Long eventId) {
        try {
            simulationService.stopSimulation(eventId);
            return ResponseEntity.ok("Simulation stopped for event: " + eventId);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/logs/{eventId}")
    public ResponseEntity<List<String>> getEventLogs(@PathVariable Long eventId) {
        Queue<String> logs = simulationService.getEventLogs(eventId);
        if (logs == null || logs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of("No logs available.")); // Check for empty logs
        }
        return ResponseEntity.ok(new ArrayList<>(logs));
    }

}
