package org.aub.telegram.bot.stats;

import java.util.*;

public class StatisticService {
    private List<Entry> allStats = new ArrayList<>();

    public void addStat(Entry entry) {
        allStats.add(entry);
    }

    public List<Entry> getAllStats() {
        return allStats;
    }

}
