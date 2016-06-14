package org.aub.telegram.bot.alias.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class Game {
    private Team currentTeam;
    private Team[] teams;

    public void addNewTeams(int numberOfTeams) {
        teams = new Team[numberOfTeams];
        for (int i = 0; i < numberOfTeams; i++) {
            teams[i] = new Team(i, "Team " + (i + 1));
        }
        currentTeam = teams[0];
    }

    public void startNewRound() {
        currentTeam.getRounds().add(new Round());
    }

    public boolean isCurrentRoundFinished() {
        return Duration.between(currentTeam.getLastRound().getStartedAt().toInstant(), Instant.now()).toMillis() > 10000;
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

}
