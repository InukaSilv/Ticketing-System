package com.ticketing.system.ticketing_backend.service;

import com.ticketing.system.ticketing_backend.model.Event;
import com.ticketing.system.ticketing_backend.model.Ticket;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Queue;

public class Customer implements Runnable {
    private final Event event;
    private final int buyingRate; // Add this
    private final Queue<String> logs;
    private final SimpMessagingTemplate messagingTemplate;

    public Customer(Event event, int buyRate, Queue<String> logs, SimpMessagingTemplate messagingTemplate) {
        this.event = event;
        this.buyingRate = buyRate; // Assign it here
        this.logs = logs;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (event.getReleasedTicketQueue()) {
                    while (event.getReleasedTicketQueue().isEmpty()) {
                        logs.add("[Customer] Waiting for tickets to be available.");
                        messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), "[Customer] Waiting for tickets to be available.");
                        event.getReleasedTicketQueue().wait(); // Wait for tickets to be released
                    }

                    // Purchase the ticket from the released queue
                    Ticket purchasedTicket = event.getReleasedTicketQueue().remove(0);
                    purchasedTicket.setStatus("Purchased");

                    String logEntry = "[Customer] Purchased ticket #" + purchasedTicket.getId();
                    logs.add(logEntry);
                    messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), logEntry);

                    event.getReleasedTicketQueue().notifyAll(); // Notify Vendor if necessary
                }
                Thread.sleep(buyingRate * 1000L); // Simulate purchase delay
            }
        } catch (InterruptedException e) {
            logs.add("[Customer] Thread interrupted. Exiting.");
            messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), "[Customer] Thread interrupted. Exiting.");
            Thread.currentThread().interrupt();
        }
    }
}
