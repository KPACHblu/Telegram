package org.aub.telegram.bot.stats;

import org.telegram.telegrambots.api.objects.Update;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class StatisticService {
    private List<Entry> allStats = new ArrayList<>();
    private static final String STATISTIC_MESSAGE_REQUEST = "Password@!";

    public boolean isRequestForStatistic(Update update) {
        return update.getMessage().getText().contains(STATISTIC_MESSAGE_REQUEST);
    }

    public String getStatistic(String message) {
        String result = null;
        String argument = message.split(" ")[1];
        switch (argument) {
            case "day" :
                result = getStatsForDay().toString();
                break;
            case "week":
                result = getStatsForWeek().toString();
                break;
            case "month":
                result = getStatsForMonth().toString();
                break;
        }
        return result;
    }

    public void addStat(Entry entry) {
        allStats.add(entry);
    }

    public List<Entry> getAllStats() {
        return allStats;
    }

    public Map<String, Integer> getStatsForDay() {
        return getStatsForPeriod(1);
    }

    public Map<String, Integer> getStatsForWeek() {
        return getStatsForPeriod(7);
    }

    public Map<String, Integer> getStatsForMonth() {
        return getStatsForPeriod(31);
    }

    private Map<String, Integer> getStatsForPeriod(int period) {
        Map<String, Integer> result = new HashMap<>();
        for (Entry entry : allStats) {
            boolean isOneDay = Duration.between(entry.getDate().toInstant(), Instant.now()).toDays() <= period;
            if (isOneDay) {
                Integer amount = result.get(entry.getUserId());
                result.put(entry.getUserId(), amount == null? 1 : amount + 1);
            }
        }
        return result;
    }

}
