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
            config.createNewConfig(scanner);
        }
        config.saveConfiguration();

        Ticketpool ticketpool = new Ticketpool(config.getMaxTicketCapacity());
        System.out.println("System configured!. Run command 'start' to begin and 'stop' to exit.");

        while (true) {
            System.out.println("Enter command (start/stop) : ");
            String cmd = scanner.nextLine();

            if ("start".equalsIgnoreCase(cmd)) {

            }
        }
    }
}
