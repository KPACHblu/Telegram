package org.aub.telegram.bot;

import org.aub.telegram.bot.joke.JokeBot;
import org.aub.telegram.bot.puzzle.PuzzleBot;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

public class Main {
    public static void main(String[] args) {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new JokeBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
