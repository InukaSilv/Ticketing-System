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
        Thread vendorThread = new Thread(new Vendor(ticketpool, config.getTicketReleaseRate(), config.getTotalTickets()));
        Thread[] customerThread = {null}; // Wrapping for restartable logic

        vendorThread.start();
        customerThread[0] = startCustomerThread(ticketpool, config);

        System.out.println("Ticketing system started! Vendor releasing tickets.");

        try {
            vendorThread.join(); // Wait for vendor thread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor thread interrupted.");
        }

        // Loop for prompting ticket additions and restarting vendors
        while (true) {
            synchronized (ticketpool) {
                if (ticketpool.getTicketCount() == 0 && !vendorThread.isAlive()) {
                    System.out.println("All tickets sold! Do you want to add more tickets? (Y/N): ");
                    String addChoice = scanner.nextLine().toLowerCase();
                    if ("y".equals(addChoice)) {
                        // Calculate correct available capacity
                        int availableCapacity = config.getMaxTicketCapacity() - config.getTotalTickets();
                        System.out.print("Enter additional tickets to add (maximum " + availableCapacity + "): ");
                        int newTickets = validateInput(scanner);

                        if (newTickets <= availableCapacity) {
                            config.incrementTotalTickets(newTickets);

                            // Restart vendor to release new tickets
                            Thread newVendorThread = new Thread(new Vendor(ticketpool, config.getTicketReleaseRate(), newTickets));
                            newVendorThread.start();

                            try {
                                newVendorThread.join(); // Wait for new vendor thread to finish
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                System.out.println("New vendor thread interrupted.");
                            }

                            // Restart customer thread if needed
                            if (customerThread[0] == null || !customerThread[0].isAlive()) {
                                customerThread[0] = startCustomerThread(ticketpool, config);
                            }

                            System.out.println("Vendor added tickets. Resuming customer purchases.");
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
