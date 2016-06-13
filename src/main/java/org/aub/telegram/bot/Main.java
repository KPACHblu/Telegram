package org.aub.telegram.bot;

import org.aub.telegram.bot.aphorism.AphorismBot;
import org.aub.telegram.bot.joke.JokeBot;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.logging.BotLogger;

public class Main {
    private static final String TAG = "Main";

    public static void main(String[] args) {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new AphorismBot());
            telegramBotsApi.registerBot(new JokeBot());
        } catch (TelegramApiException e) {
            BotLogger.error(TAG, "Can't register bots", e);
        }
    }

}
