package org.aub.telegram.bot.alias;

import org.telegram.telegrambots.logging.BotLogger;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class AliasDao {
    private static final String TAG = "AliasDao";
    private Random random = new Random();
    private String[] allJokes;

    public AliasDao() {
        String file = getFile("alias-bot/words.txt");
        allJokes = file.split("\n");
        BotLogger.info(TAG, "Initialization of DB is done. Size: " + allJokes.length);
    }

    public String getRandomWord() {
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
