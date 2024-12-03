import java.util.concurrent.ConcurrentLinkedQueue;

public class Ticketpool {
    private final ConcurrentLinkedQueue<Integer> Tickets = new ConcurrentLinkedQueue<>();
    private final int maxCapacity;

    public Ticketpool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public synchronized boolean addTicket(int ticketID) {
        if (Tickets.size() < maxCapacity) {
            Tickets.add(ticketID);
            return true;
        }
        return false; // Indicate that adding a ticket was unsuccessful
    }

    public synchronized Integer retrieveTicket() {
        return Tickets.poll();
    }

    public int getTicketCount() {
        return Tickets.size();
    }

    public synchronized boolean isFull() {
        return Tickets.size() >= maxCapacity;
    }
}
