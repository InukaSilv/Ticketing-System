import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerBuyRate;
    private int maxTicketCapacity;
    private static final String CONFIG_FILE = "config.json";

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

    public void incrementTotalTickets(int increment) {
        this.totalTickets += increment;
    }

    public void saveConfiguration() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            String json = String.format("{\"totalTickets\":%d,\"ticketReleaseRate\":%d,\"customerBuyRate\":%d,\"maxTicketCapacity\":%d}",
                    totalTickets, ticketReleaseRate, customerBuyRate, maxTicketCapacity);
            writer.write(json);
            System.out.println("Configuration successfully saved to JSON-like format!");
        } catch (IOException e) {
            System.err.println("Failed to save configuration: " + e.getMessage());
        }
    }

    public static Configuration loadConfiguration() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            System.out.println("No Configuration found. Starting new sequence!");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String json = reader.readLine();
            Map<String, Integer> configMap = parseJson(json);
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

    private static Map<String, Integer> parseJson(String json) {
        Map<String, Integer> map = new HashMap<>();
        json = json.replaceAll("[{}\"]", ""); // Remove braces and quotes
        String[] entries = json.split(",");
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            map.put(keyValue[0], Integer.parseInt(keyValue[1]));
        }
        return map;
    }

    public void printConfig() {
        System.out.println("Current Configuration:");
        System.out.println("Total tickets: " + totalTickets);
        System.out.println("Ticket release rate: " + ticketReleaseRate);
        System.out.println("Customer buy rate: " + customerBuyRate);
        System.out.println("Max ticket capacity: " + maxTicketCapacity);
    }
}
