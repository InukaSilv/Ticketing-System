import java.util.Scanner;

public class TicketingSystemCLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Configuration config;

        System.out.println("Welcome to the Real-time Ticketing System!");
        System.out.println("Would you like to load previous configuration settings? (Y/N): ");
        String choice = scanner.nextLine().toLowerCase();

        if ("y".equals(choice)) {
            config = Configuration.loadconfiguration();
            if (config == null) {
                config = createNewConfig(scanner);
            } else {
                config.printConfig();
            }
        } else {
            config = createNewConfig(scanner);
        }
        config.saveConfiguration();

        Ticketpool ticketpool = new Ticketpool(config.getMaxTicketCapacity());
        System.out.println("System configured!. Run command 'start' to begin and 'stop' to exit.");

        while (true) {
            System.out.println("Enter command (start/stop) : ");
            String cmd = scanner.nextLine();

            if ("start".equalsIgnoreCase(cmd)) {
            Thread vendorThread = new Thread(new Vendor(ticketpool, config.getTicketReleaseRate()));
            Thread customerThread = new Thread(new Customer(ticketpool, config.getCustomerBuyRate()));

            vendorThread.start();
            customerThread.start();

            Logger.log("Ticketing System initiated!");
            System.out.println("Ticketing system started! Type 'stop' to terminate.");
            
            } else if ("stop".equalsIgnoreCase(cmd)) {
                Logger.log("Ticketing System stopped!");
                break;
            } else {
                System.out.println("Invalid command. Please enter 'start' or 'stop'.");
            }
        }

        Logger.close();
        scanner.close();
    }

    private static Configuration createNewConfig(Scanner scanner) {
        System.out.print("Enter Total Tickets : ");
        int totalTickets = validateInput(scanner);

        System.out.print("Enter ticket release rate (seconds) : ");
        int ticketReleaseRate = validateInput(scanner);

        System.out.print("Enter customer buy rate (seconds) : ");
        int customerBuyRate = validateInput(scanner);

        System.out.print("Enter max ticket capacity : ");
        int maxCapacity = validateInput(scanner);

        return new Configuration(totalTickets, ticketReleaseRate, customerBuyRate, maxCapacity);
    }

    private static int validateInput(Scanner scanner) {
        while (true) {
            try {
                int Input = Integer.parseInt(scanner.nextLine());
                if (Input > 0) return Input;
                System.out.print("Invalid input. Enter a positive integer: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a positive integer: ");
            }
        }
    }

}
