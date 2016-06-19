package org.aub.telegram.bot.joke;

import org.telegram.telegrambots.logging.BotLogger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class JokeDao {
    private static final String TAG = "JokeBot";
    private Random random = new Random();
    private String[] allJokes;

    public JokeDao() {
        String file = getFile("joke-bot/jokes.txt");
        allJokes = file.split("\n\n");
        BotLogger.info(TAG, "Initialization of DB is done");
    }

    public String getRandomJoke() {
        return allJokes[random.nextInt(allJokes.length - 1)];
    }

    private String getFile(String fileName) {
        StringBuilder result = new StringBuilder("");
        ClassLoader classLoader = getClass().getClassLoader();
        try (Scanner scanner = new Scanner(classLoader.getResourceAsStream(fileName), StandardCharsets.UTF_8.displayName())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        }
        return result.toString();
    }

}
