package com.ticketing.system.ticketing_backend.service;

import com.ticketing.system.ticketing_backend.model.Event;
import com.ticketing.system.ticketing_backend.model.Ticket;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Queue;

/**
 * Vendor class simulates the vendor releasing tickets into the system.
 * The vendor thread waits for the ticket pool to have tickets and releases them one by one.
 */
public class Vendor implements Runnable {
    private final Event event;
    private final int releaseRate; // Release rate (time in seconds between each ticket release)
    private final Queue<String> logs;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructor for the Vendor.
     *
     * @param event the event associated with the tickets
     * @param releaseRate the rate at which tickets are released (in seconds)
     * @param logs a queue to store logs related to the vendor's actions
     * @param messagingTemplate a template for sending messages to connected clients
     */
    public Vendor(Event event, int releaseRate, Queue<String> logs, SimpMessagingTemplate messagingTemplate) {
        this.event = event;
        this.releaseRate = releaseRate; // Assign the release rate
        this.logs = logs;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * The run method simulates the vendor releasing tickets to the system.
     * The vendor will keep releasing tickets until the thread is interrupted.
     */
    @Override
    public void run() {
        try {
            // Keep running until the thread is interrupted
            while (!Thread.currentThread().isInterrupted()) {
                // Synchronize access to the ticket pool
                synchronized (event.getTicketPool()) {
                    // Wait until there are tickets available in the pool
                    while (event.getTicketPool().isEmpty()) {
                        logs.add("[Vendor] Waiting - Ticket pool is empty.");
                        messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), "[Vendor] Waiting - Ticket pool is empty.");
                        event.getTicketPool().wait(); // Wait for tickets to be added
                    }

                    // Retrieve the next available ticket from the pool
                    Ticket ticketToRelease = event.getTicketPool().remove(0);

                    // Synchronize access to the released ticket queue
                    synchronized (event.getReleasedTicketQueue()) {
                        event.getReleasedTicketQueue().add(ticketToRelease);
                        logs.add("[Vendor] Released ticket #" + ticketToRelease.getId());
                        messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), "[Vendor] Released ticket #" + ticketToRelease.getId());

                        // Notify customers that a ticket is released
                        event.getReleasedTicketQueue().notifyAll();
                    }
                }

                // Simulate the delay between ticket releases
                Thread.sleep(releaseRate * 1000L);
            }
        } catch (InterruptedException e) {
            // Handle thread interruption
            logs.add("[Vendor] Thread interrupted. Exiting.");
            messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), "[Vendor] Thread interrupted. Exiting.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }
}