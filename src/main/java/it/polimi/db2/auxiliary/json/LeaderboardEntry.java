package it.polimi.db2.auxiliary.json;

import java.io.Serializable;

public class LeaderboardEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String userId;
    private final int score;

    public LeaderboardEntry(String userId, int score){
        this.userId = userId;
        this.score = score;
    }
}
