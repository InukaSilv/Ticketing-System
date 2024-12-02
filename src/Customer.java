public class Customer implements Runnable{
    private final Ticketpool ticketpool;
    private final int customerBuyRate;

    public Customer(Ticketpool ticketpool, int customerBuyRate) {
        this.ticketpool = ticketpool;
        this.customerBuyRate = customerBuyRate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(customerBuyRate * 1000L);
                Integer Ticket = ticketpool.retrieveTicket();
                if (Ticket != null) {
                    String msg = "Customer purchased ticket #" + Ticket;
                    Logger.log(msg);
                } else {
                    Logger.log("Customer attempted to purchase a ticket, none were available.")
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
