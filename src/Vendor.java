public class Vendor implements Runnable {
    private final Ticketpool ticketpool;
    private final int ticketReleaseRate;
    private int totalTicketsToRelease; // Tracks how many tickets remain to release
    private int ticketID; // Starting ticket ID

    public Vendor(Ticketpool ticketpool, int ticketReleaseRate, int startTicketID, int totalTicketsToRelease) {
        this.ticketpool = ticketpool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketID = startTicketID;
        this.totalTicketsToRelease = totalTicketsToRelease;
    }

    public synchronized void addTickets(int additionalTickets) {
        this.totalTicketsToRelease += additionalTickets; // Increment the number of tickets to release
        notify();
    }

    @Override
    public void run() {
        try {
            while (totalTicketsToRelease > 0) {
                synchronized (ticketpool) {
                    while (ticketpool.isFull()) {
                        System.out.println("Vendor waiting - Ticket pool is full.");
                        ticketpool.wait(); // Wait for capacity
                    }
                }

                // Simulate releasing a ticket
                Thread.sleep(ticketReleaseRate * 1000L);

                synchronized (ticketpool) {
                    if (ticketpool.addTicket(ticketID)) {
                        System.out.println("Vendor added ticket #" + ticketID);
                        ticketID++;
                        totalTicketsToRelease--;
                        ticketpool.notifyAll(); // Notify customers
                    }
                }
            }
            System.out.println("Vendor thread exiting - Total tickets released.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor thread interrupted.");
        }
    }
}
