public class Vendor implements Runnable {
    private final Ticketpool ticketpool;
    private final int ticketReleaseRate;
    private final int totalTickets;
    private int ticketID = 1;

    public Vendor(Ticketpool ticketpool, int ticketReleaseRate, int totalTickets) {
        this.ticketpool = ticketpool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTickets = totalTickets;
    }

    @Override
    public void run() {
        try {
            while (ticketID <= totalTickets) {
                synchronized (ticketpool) {
                    while (ticketpool.isFull()) {
                        Logger.log("Vendor waiting - Ticket pool is full.");
                        ticketpool.wait(); // Wait for capacity to free up
                    }
                }

                // Add ticket
                Thread.sleep(ticketReleaseRate * 1000L);
                if (ticketpool.addTicket(ticketID)) {
                    Logger.log("Vendor added ticket #" + ticketID);
                    ticketID++;
                }

                // Notify waiting customers
                synchronized (ticketpool) {
                    ticketpool.notifyAll();
                }
            }
            Logger.log("Vendor thread exiting - Total tickets released.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.log("Vendor thread interrupted.");
        }
    }
}
