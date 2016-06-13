package org.aub.telegram.bot.stats;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.logging.BotLogger;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticService {
    private static final String TAG = "StatisticService";
    private static final String STATISTIC_MESSAGE_REQUEST = "Password@!";
    private static final String REQUEST_PARAM_DAY = "day";
    private static final String REQUEST_PARAM_WEEK = "week";
    private static final String REQUEST_PARAM_MONTH = "month";

    private List<Entry> allStats = new ArrayList<>();

    public boolean sendStatisticIfNeeded(Update update, AbsSender bot) {
        boolean result = false;
        String messageText = update.getMessage().getText();
        String userId = String.valueOf(update.getMessage().getFrom().getId());
        if (messageText.contains(STATISTIC_MESSAGE_REQUEST)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(userId);
            sendMessage.setText(getStatistic(messageText));
            try {
                bot.sendMessage(sendMessage);
                result = true;
            } catch (TelegramApiException e) {
                BotLogger.error(TAG, e.getMessage());
            }
        } else {
            addStat(new Entry(userId, messageText));
        }
        return result;
    }

    private String getStatistic(String message) {
        String result = null;
        String argument = message.split(" ")[1];
        switch (argument) {
            case REQUEST_PARAM_DAY:
                result = getStatsForDay().toString();
                break;
            case REQUEST_PARAM_WEEK:
                result = getStatsForWeek().toString();
                break;
            case REQUEST_PARAM_MONTH:
                result = getStatsForMonth().toString();
                break;
        }
        return result;
    }

    private void addStat(Entry entry) {
        allStats.add(entry);
    }

    private Summary getStatsForDay() {
        return getStatsForPeriod(1);
    }

    private Summary getStatsForWeek() {
        return getStatsForPeriod(7);
    }

    private Summary getStatsForMonth() {
        return getStatsForPeriod(31);
    }

    private Summary getStatsForPeriod(int period) {
        Map<String, Integer> personToRequest = new HashMap<>();
        for (Entry entry : allStats) {
            boolean isPeriod = Duration.between(entry.getDate().toInstant(), Instant.now()).toDays() <= period;
            if (isPeriod) {
                Integer amount = personToRequest.get(entry.getUserId());
                personToRequest.put(entry.getUserId(), amount == null ? 1 : amount + 1);
            }
        }
        int maxRequestPerPerson = 0;
        int minRequestPerPerson = 1;
        int allRequest = 0;
        for (Map.Entry<String, Integer> entry : personToRequest.entrySet()) {
            Integer requests = entry.getValue();
            allRequest += requests;
            if (requests > maxRequestPerPerson) {
                maxRequestPerPerson = requests;
                continue;
            }

            if (requests < minRequestPerPerson) {
                minRequestPerPerson = requests;
            }
        }
        int people = personToRequest.size();
        return new Summary(people, allRequest, allRequest / people, minRequestPerPerson, maxRequestPerPerson);
    }

}
