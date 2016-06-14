package org.aub.telegram.bot.alias;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class Game {
    private int currentRound;
    private Team currentTeam;
    private Date currentRoundStart;
    private Team[] teams;

    public void addNewTeams(int numberOfTeams) {
        teams = new Team[numberOfTeams];
        for (int i = 0; i < numberOfTeams; i++) {
            teams[i] = new Team(i + 1, "Team " + i + 1);
        }
        currentTeam = teams[0];
    }

    public void startNewRound() {
        currentRoundStart = new Date();
    }

    public boolean isCurrentRoundFinished() {
        return Duration.between(currentRoundStart.toInstant(), Instant.now()).toMillis() > 60000;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    //TODO
    public void changeTeam() {
        for (int i=0; i<teams.length; i++) {
            if (teams[i].getRounds() < currentRound) {
                currentTeam = teams[i];
                break;
            }
        }
    }

}
