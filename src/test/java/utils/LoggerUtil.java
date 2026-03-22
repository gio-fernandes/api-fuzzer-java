package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LoggerUtil {

    private static final String LOG_FILE = "fuzzer-results.log";

    public static void saveLog(String content) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println(content);

        } catch (IOException e) {
            System.err.println("Erro ao salvar log: " + e.getMessage());
        }
    }
}