package org.aub.telegram.bot.alias.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game {
    private static final int ROUND_TIME_IN_MILLIS = 60000;
    private static final int POINTS_FOR_WIN = 30;
    private Team currentTeam;
    private Team[] teams;

    public void addNewTeams(String[] teamNames) {
        teams = new Team[teamNames.length];
        for (int i = 0; i < teamNames.length; i++) {
            teams[i] = new Team(i, teamNames[i]);
        }
        currentTeam = teams[0];
    }

    public void startNewRound() {
        currentTeam.getRounds().add(new Round());
    }

    public boolean isCurrentRoundFinished() {
        if (currentTeam != null && currentTeam.getLastRound() != null) {
            return Duration.between(currentTeam.getLastRound().getStartedAt().toInstant(), Instant.now()).toMillis() > ROUND_TIME_IN_MILLIS;
        }
        return false;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public void changeTeam() {
        int id = currentTeam.getId();
        //If current team is the last - choose the first
        if (id == teams.length - 1) {
            currentTeam = teams[0];
        } else {
            currentTeam = teams[id + 1];
        }
    }

    public boolean isGameFinished() {
        int rounds = -1;
        boolean result = false;
        for (Team current : teams) {
            if (current.getPoints() >= POINTS_FOR_WIN) {
                if (rounds == -1) {
                    rounds = current.getRounds().size();
                    result = true;
                    continue;
                }
            }
            if (current.getRounds().size() < rounds) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * @return List of Teams with points equals/over 100
     */
    public List<Team> getWinners() {
        List<Team> winners = new ArrayList<>();
        for (Team current : this.teams) {
            if (current.getPoints() >= POINTS_FOR_WIN) {
                winners.add(current);
            }
        }
        return winners;
    }

}
