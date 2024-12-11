package com.ticketing.system.ticketing_backend.service;

import com.ticketing.system.ticketing_backend.model.Event;
import com.ticketing.system.ticketing_backend.model.Ticket;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Queue;

public class Vendor implements Runnable {
    private final Event event;
    private final int releaseRate; // Add this
    private final Queue<String> logs;
    private final SimpMessagingTemplate messagingTemplate;

    public Vendor(Event event, int releaseRate, Queue<String> logs, SimpMessagingTemplate messagingTemplate) {
        this.event = event;
        this.releaseRate = releaseRate; // Assign it here
        this.logs = logs;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (event.getTicketPool()) {
                    while (event.getTicketPool().isEmpty()) {
                        logs.add("[Vendor] Waiting - Ticket pool is empty.");
                        messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), "[Vendor] Waiting - Ticket pool is empty.");
                        event.getTicketPool().wait(); // Wait for tickets to be added
                    }

                    // Retrieve the next ticket from ticketPool
                    Ticket ticketToRelease = event.getTicketPool().remove(0);

                    // Add the ticket to the released queue
                    synchronized (event.getReleasedTicketQueue()) {
                        event.getReleasedTicketQueue().add(ticketToRelease);
                        logs.add("[Vendor] Released ticket #" + ticketToRelease.getId());
                        messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), "[Vendor] Released ticket #" + ticketToRelease.getId());

                        event.getReleasedTicketQueue().notifyAll(); // Notify customers about the released ticket
                    }
                }
                Thread.sleep(releaseRate * 1000L); // Simulate release delay
            }
        } catch (InterruptedException e) {
            logs.add("[Vendor] Thread interrupted. Exiting.");
            messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), "[Vendor] Thread interrupted. Exiting.");
            Thread.currentThread().interrupt();
        }
    }
}