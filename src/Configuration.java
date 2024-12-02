import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

public class Configuration  {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerBuyRate;
    private int maxTicketCapacity;
    private static final String CONFIG_FILE = "config.txt";

    public Configuration(int totalTickets, int ticketReleaseRate, int customerBuyRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerBuyRate = customerBuyRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    public int getCustomerBuyRate() {
        return customerBuyRate;
    }
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void saveConfiguration() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write("totalTickets=" + totalTickets);
            writer.newLine();
            writer.write("ticketReleaseRate=" + ticketReleaseRate);
            writer.newLine();
            writer.write("customerBuyRate=" + customerBuyRate);
            writer.newLine();
            writer.write("maxTicketCapacity=" + maxTicketCapacity);
            writer.newLine();
            System.out.println("Configuration successfully saved!");
        } catch (IOException e) {
            System.err.println("Failed to save configuration: " + e.getMessage());
        }
    }

    public static Configuration loadconfiguration() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            System.out.println("No Configuration found. Starting new sequence!");
            return null;
        }

        Map<String, Integer> configMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    configMap.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
            return new Configuration(
                    configMap.getOrDefault("totalTickets", 0),
                    configMap.getOrDefault("ticketReleaseRate", 1),
                    configMap.getOrDefault("customerBuyRate", 1),
                    configMap.getOrDefault("maxTicketCapacity", 0)
            );
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
        return null;
    }

    public void printConfig() {
        System.out.println("Current Configuration : ");
        System.out.println("Total tickets : " + totalTickets);
        System.out.println("Ticket release rate : " + ticketReleaseRate);
        System.out.println("Customer buy rate : " + customerBuyRate);
        System.out.println("Max ticket capacity : " + maxTicketCapacity);
    }
}
