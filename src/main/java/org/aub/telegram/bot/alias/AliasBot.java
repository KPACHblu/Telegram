package org.aub.telegram.bot.alias;

import org.aub.telegram.bot.stats.StatisticService;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AliasBot extends TelegramLongPollingBot {
    private static final String TAG = "AliasBot";
    private StatisticService statisticService = new StatisticService();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void onUpdateReceived(Update update) {
        if (statisticService.sendStatisticIfNeeded(update, this)) {
            return;
        }
        sendSome();
    }

    @Override
    public String getBotUsername() {
        return "WhiskyAliasBot";
    }

    @Override
    public String getBotToken() {
        return "122934009:AAHEM7mI69BojtIsVrg6kyD7lDaDTrmdQh4";
    }

    private void sendSome() {

    }

    private static class TimerJob implements Runnable {

        @Override
        public void run() {

        }
    }
}
