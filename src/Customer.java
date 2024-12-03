public class Customer implements Runnable {
    private final Ticketpool ticketpool;
    private final int customerBuyRate;
    private boolean ticketsAvailable = false; // Tracks if tickets have been available

    public Customer(Ticketpool ticketpool, int customerBuyRate) {
        this.ticketpool = ticketpool;
        this.customerBuyRate = customerBuyRate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (ticketpool) {
                    while (ticketpool.getTicketCount() == 0) {
                        if (!Thread.currentThread().isInterrupted()) {
                            System.out.println("Customer waiting for tickets...");
                            ticketpool.wait();
                        } else {
                            return; // Exit if no tickets and interrupted
                        }
                    }
                }

                // Simulate ticket purchase
                Thread.sleep(customerBuyRate * 1000L);
                Integer ticket = ticketpool.retrieveTicket();
                if (ticket != null) {
                    System.out.println("Customer purchased ticket #" + ticket);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Customer thread interrupted.");
        }
    }
}