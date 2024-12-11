import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "Ticketing_system.log";
    private static BufferedWriter writer;

    static {
        try {
            writer = new BufferedWriter(new FileWriter(LOG_FILE, true));
        } catch (IOException e) {
            System.out.println("Failed to initialize log file: " + e.getMessage());
        }
    }

    public static synchronized void log(String msg) {
        try {
            String DateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String logMsg = String.format("[%s] %s", DateTime, msg);
            System.out.println(logMsg);
            writer.write(logMsg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to write to the log file: " + e.getMessage());
        }
    }

    public static void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to close log file: " + e.getMessage());
        }
    }
}
