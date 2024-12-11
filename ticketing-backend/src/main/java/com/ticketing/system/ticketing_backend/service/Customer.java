package com.ticketing.system.ticketing_backend.service;

import com.ticketing.system.ticketing_backend.model.Event;
import com.ticketing.system.ticketing_backend.model.Ticket;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Queue;

/**
 * Represents a customer who purchases tickets from the released ticket queue.
 * Implements Runnable to allow the customer to run on a separate thread.
 */
public class Customer implements Runnable {

    private final Event event;
    private final int buyingRate; // Time between purchases in seconds
    private final Queue<String> logs;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructs a Customer object.
     *
     * @param event           the event for which tickets are being purchased
     * @param buyRate         the rate at which the customer purchases tickets
     * @param logs            the log queue to store customer actions
     * @param messagingTemplate the messaging template to send logs to the frontend
     */
    public Customer(Event event, int buyRate, Queue<String> logs, SimpMessagingTemplate messagingTemplate) {
        this.event = event;
        this.buyingRate = buyRate;
        this.logs = logs;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Runs the customer thread to simulate ticket purchasing.
     * The customer waits for tickets to be released and then buys them.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (event.getReleasedTicketQueue()) {
                    // Wait for tickets to be available in the released ticket queue
                    while (event.getReleasedTicketQueue().isEmpty()) {
                        logs.add("[Customer] Waiting for tickets to be available.");
                        messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), "[Customer] Waiting for tickets to be available.");
                        event.getReleasedTicketQueue().wait(); // Wait for tickets to be released
                    }

                    // Purchase the ticket from the released queue
                    Ticket purchasedTicket = event.getReleasedTicketQueue().remove(0);
                    purchasedTicket.setStatus("Purchased");

                    // Log the ticket purchase
                    String logEntry = "[Customer] Purchased ticket #" + purchasedTicket.getId();
                    logs.add(logEntry);
                    messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), logEntry);

                    // Notify vendor if necessary (not implemented)
                    event.getReleasedTicketQueue().notifyAll();
                }
                Thread.sleep(buyingRate * 1000L); // Simulate delay between purchases
            }
        } catch (InterruptedException e) {
            // Log interruption and exit
            logs.add("[Customer] Thread interrupted. Exiting.");
            messagingTemplate.convertAndSend("/topic/logs/" + event.getId(), "[Customer] Thread interrupted. Exiting.");
            Thread.currentThread().interrupt();
        }
    }
}
