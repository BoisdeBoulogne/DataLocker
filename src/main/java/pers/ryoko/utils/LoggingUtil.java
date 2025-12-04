package pers.ryoko.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingUtil {

    private static final Path LOG_DIR =
            Paths.get(System.getenv("LOCALAPPDATA"), "DataLocker");

    private static final Path LOG_FILE = LOG_DIR.resolve("DataLocker.log");


    private static final DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            if (!Files.exists(LOG_DIR)) {
                Files.createDirectories(LOG_DIR);
            }
            if (!Files.exists(LOG_FILE)) {
                Files.createFile(LOG_FILE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void info(String msg) { write("INFO", msg, null); }

    public static void error(String msg, Throwable t) { write("ERROR", msg, t); }

    public static void debug(String msg) { write("DEBUG", msg, null); }

    private static void write(String level, String msg, Throwable t) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE.toFile(), true))) {
            bw.write("[" + fmt.format(LocalDateTime.now()) + "] [" + level + "] " + msg);
            bw.newLine();
            if (t != null) {
                bw.write("Exception: " + t.toString());
                bw.newLine();
                for (StackTraceElement ste : t.getStackTrace()) {
                    bw.write("    at " + ste);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // fallback
        }
    }
}
