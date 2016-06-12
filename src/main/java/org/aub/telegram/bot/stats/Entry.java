package org.aub.telegram.bot.stats;

import java.util.Date;

public class Entry {
    private String command;
    private String userId;
    private Date date;

    public Entry(String userId, String command) {
        this.command = command;
        this.userId = userId;
        this.date = new Date();
    }

    public String getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "command='" + command + '\'' +
                ", userId='" + userId + '\'' +
                ", date=" + date +
                '}';
    }
}
