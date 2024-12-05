package com.ticketing.system.ticketing_backend.controller;

import com.ticketing.system.ticketing_backend.model.Event;
import com.ticketing.system.ticketing_backend.model.User;
import com.ticketing.system.ticketing_backend.repository.EventRepository;
import com.ticketing.system.ticketing_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public String createEvent(@RequestParam String username, @RequestParam String eventName, @RequestParam int maxCapacity) {
        User vendor = userRepository.findByUsername(username);
        if (vendor == null || !vendor.getRole().equals(User.Role.VENDOR)) {
            return "Invalid vendor!";
        }

        Event event = new Event();
        event.setName(eventName);
        event.setMaxCapacity(maxCapacity);
        event.setVendor(vendor);
        eventRepository.save(event);
        return "Event created successfully!";
    }
}
