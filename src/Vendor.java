public class Vendor implements Runnable{
    private final Ticketpool ticketpool;
    private final int ticketReleaseRate;
    private int tickedID = 1;

    public Vendor(Ticketpool ticketpool, int ticketReleaseRate) {
        this.ticketpool = ticketpool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(ticketReleaseRate * 1000L);
                if (ticketpool.addTicket(tickedID)) {
                    String msg = "Vendor added ticket #" + tickedID;
                    Logger.log(msg);
                    tickedID++;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
