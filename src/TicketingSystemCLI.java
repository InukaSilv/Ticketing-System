import java.util.Scanner;

public class TicketingSystemCLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Configuration config = null;

        System.out.println("Welcome to the Real-time Ticketing System!");
        System.out.println("Would you like to load previous configuration settings? (Y/N): ");
        String choice = scanner.nextLine().toLowerCase();

        while (true) {
            if ("y".equals(choice)) {
                config = Configuration.loadConfiguration();
                if (config == null) {
                    config = createNewConfig(scanner);
                    break;
                } else {
                    config.printConfig();
                    break;
                }
            } else if ("n".equalsIgnoreCase(choice)) {
                config = createNewConfig(scanner);
                break;
            } else {
                System.out.println("Invalid input. Please enter 'y' / 'n'.");
                choice = scanner.nextLine().toLowerCase();
            }
        }

        config.saveConfiguration();

        Ticketpool ticketpool = new Ticketpool(config.getMaxTicketCapacity());
        System.out.println("System configured! Run command 'start' to begin and 'stop' to exit.");

        boolean running = true;

        while (running) {
            System.out.println("Enter command (start/stop): ");
            String cmd = scanner.nextLine();

            if ("start".equalsIgnoreCase(cmd)) {
                startTicketingSystem(ticketpool, config, scanner);
            } else if ("stop".equalsIgnoreCase(cmd)) {
                System.out.println("Exiting Ticketing System...");
                running = false;
            } else {
                System.out.println("Invalid command. Please enter 'start' or 'stop'.");
            }
        }

        scanner.close();
    }

    private static void startTicketingSystem(Ticketpool ticketpool, Configuration config, Scanner scanner) {
        int currentTicketID = 1;
        Vendor vendor = new Vendor(ticketpool, config.getTicketReleaseRate(), currentTicketID, config.getTotalTickets());
        Thread vendorThread = new Thread(vendor);
        Thread customerThread = startCustomerThread(ticketpool, config);

        vendorThread.start();

        System.out.println("Ticketing system started! Vendor releasing tickets...");

        while (true) {
            try {
                vendorThread.join(); // Wait for vendor thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor thread interrupted.");
            }

            synchronized (ticketpool) {
                if (ticketpool.getTicketCount() == 0 && !vendorThread.isAlive()) {
                    System.out.println("All tickets sold! Do you want to add more tickets? (Y/N): ");
                    String addChoice = scanner.nextLine().toLowerCase();

                    if ("y".equals(addChoice)) {
                        int availableCapacity = config.getMaxTicketCapacity() - ticketpool.getTicketCount();
                        System.out.print("Enter additional tickets to add (maximum " + availableCapacity + "): ");
                        int newTickets = validateInput(scanner);

                        if (newTickets <= availableCapacity) {
                            config.incrementTotalTickets(newTickets);

                            synchronized (vendor) {
                                vendor.addTickets(newTickets); // Update total tickets in the existing vendor thread
                            }

                            // Restart vendor thread if needed
                            if (!vendorThread.isAlive()) {
                                vendorThread = new Thread(vendor);
                                vendorThread.start();
                            }

                            System.out.println("Vendor notified to release new tickets.");
                        } else {
                            System.out.println("Cannot add tickets - Exceeds max capacity.");
                        }
                    } else if ("n".equals(addChoice)) {
                        System.out.println("No additional tickets will be added. Exiting system...");
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter 'y' or 'n'.");
                    }
                }
            }
        }

        if (customerThread.isAlive()) {
            customerThread.interrupt();
            try {
                customerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer thread interrupted.");
            }
        }

        System.out.println("Exiting Ticketing System...");
    }


    private static Thread startCustomerThread(Ticketpool ticketpool, Configuration config) {
        Thread customerThread = new Thread(new Customer(ticketpool, config.getCustomerBuyRate()));
        customerThread.start();
        return customerThread;
    }

    private static Configuration createNewConfig(Scanner scanner) {
        int totalTickets, maxCapacity;

        System.out.print("Enter Total Tickets: ");
        totalTickets = validateInput(scanner);

        while (true) {
            System.out.print("Enter Max Ticket Capacity (must be greater than or equal to total tickets): ");
            maxCapacity = validateInput(scanner);
            if (maxCapacity >= totalTickets) {
                break;
            }
            System.out.println("Max ticket capacity must be greater than or equal to total tickets.");
        }

        System.out.print("Enter Ticket Release Rate (seconds): ");
        int ticketReleaseRate = validateInput(scanner);

        System.out.print("Enter Customer Buy Rate (seconds): ");
        int customerBuyRate = validateInput(scanner);

        return new Configuration(totalTickets, ticketReleaseRate, customerBuyRate, maxCapacity);
    }

    private static int validateInput(Scanner scanner) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input > 0) return input;
                System.out.print("Invalid input. Enter a positive integer: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a positive integer: ");
            }
        }
    }
}