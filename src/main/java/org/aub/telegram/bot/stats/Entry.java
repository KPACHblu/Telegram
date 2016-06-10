package org.aub.telegram.bot.stats;

import java.util.Date;

public class Entry {
    private String key;
    private String value;
    private String userId;
    private Date date;


    public Entry(String key, String value, String userId, Date date) {
        this.key = key;
        this.value = value;
        this.userId = userId;
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
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
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", userId='" + userId + '\'' +
                ", date=" + date +
                '}';
    }
}
