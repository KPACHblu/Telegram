package org.aub.telegram.bot;

import org.aub.telegram.bot.alias.AliasBot;
import org.aub.telegram.bot.aphorism.AphorismBot;
import org.aub.telegram.bot.flag.FlagBot;
import org.aub.telegram.bot.joke.JokeBot;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.File;

public class Main {
    private static final String TAG = "Main";

    public static void main(String[] args) {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
//            telegramBotsApi.registerBot(new AphorismBot());
//            telegramBotsApi.registerBot(new JokeBot());
//            telegramBotsApi.registerBot(new AliasBot());
            telegramBotsApi.registerBot(new FlagBot());
            BotLogger.info(TAG, "Bots are registered");
        } catch (Exception e) {
            BotLogger.error(TAG, "Can't register bots", e);
        }
    }

}
