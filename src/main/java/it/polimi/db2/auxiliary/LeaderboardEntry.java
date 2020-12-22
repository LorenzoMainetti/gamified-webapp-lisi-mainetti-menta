package it.polimi.db2.auxiliary;

import java.io.Serializable;

public class LeaderboardEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private int score;

    public LeaderboardEntry(String userId, int score){
        this.userId = userId;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
