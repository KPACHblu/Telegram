package org.aub.telegram.bot.alias.model;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Round {
    private Map<String, Boolean> wordToResult = new LinkedHashMap<>(); //key - question; value - is question was answered?
    private String lastAskedWord;
    private Date startedAt;

    public Round() {
        this.startedAt = new Date();
    }

    public Map<String, Boolean> getWordToResult() {
        return wordToResult;
    }

    public void setWordToResult(Map<String, Boolean> wordToResult) {
        this.wordToResult = wordToResult;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public String getLastAskedWord() {
        return lastAskedWord;
    }

    public void setLastAskedWord(String lastAskedWord) {
        this.lastAskedWord = lastAskedWord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Round round = (Round) o;

        if (wordToResult != null ? !wordToResult.equals(round.wordToResult) : round.wordToResult != null)
            return false;
        if (startedAt != null ? !startedAt.equals(round.startedAt) : round.startedAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = wordToResult != null ? wordToResult.hashCode() : 0;
        result = 31 * result + (startedAt != null ? startedAt.hashCode() : 0);
        return result;
    }
}
