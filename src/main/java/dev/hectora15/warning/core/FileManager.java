package dev.hectora15.warning.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {


    private static final String USER_HOME = System.getProperty("user.home");
    private static final Path APP_DIR = Paths.get(USER_HOME, ".warningGame");
    private static final Path SCORE_FILE = APP_DIR.resolve("highscore.dat");


    public static void initialize() {
        try {
            if (!Files.exists(APP_DIR)) {
                Files.createDirectories(APP_DIR);
            }
            if (!Files.exists(SCORE_FILE)) {
                Files.writeString(SCORE_FILE, "0");
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public static int loadHighScore() {
        try {
            if (Files.exists(SCORE_FILE)) {
                String content = Files.readString(SCORE_FILE).trim();
                return Integer.parseInt(content);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        return 0;
    }

    public static void saveHighScore(int currentScore) {
        int bestScore = loadHighScore();
        if (currentScore > bestScore) {
            try {
                Files.writeString(SCORE_FILE, String.valueOf(currentScore));
            } catch (IOException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }
}