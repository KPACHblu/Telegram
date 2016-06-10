package org.aub.telegram.bot.joke;

import org.telegram.telegrambots.logging.BotLogger;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class JokeDb {
    private static final String TAG = "JokeBot";
    private Random random = new Random();
    private String[] allJokes;

    public JokeDb() {
        String file = getFile("joke-bot/jokes.txt");
        allJokes = file.split("\n\n");
    }

    private String getFile(String fileName) {
        StringBuilder result = new StringBuilder("");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        } catch (IOException e) {
            BotLogger.error(TAG, "Reading file error:" + e.getMessage());
        }
        return result.toString();
    }

    public String getRandomJoke() {
        return allJokes[random.nextInt(allJokes.length - 1)];
    }
}
