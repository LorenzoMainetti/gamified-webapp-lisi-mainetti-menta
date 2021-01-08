package it.polimi.db2.auxiliary.json;

import it.polimi.db2.entities.Reward;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<LeaderboardEntry> leaderboard;

    public LeaderboardContent(List<Reward> leaderboard){
        this.leaderboard = convert(leaderboard);
    }

    public ArrayList<LeaderboardEntry> convert(List<Reward> info){
        if(info == null){
            return null;
        }
        else {
            ArrayList<LeaderboardEntry> leaderboard = new ArrayList<>();
            for (Reward reward : info) {
                leaderboard.add(new LeaderboardEntry(reward.getUser().getUsername(), reward.getPoints()));
            }
            return leaderboard;
        }
    }

    public ArrayList<LeaderboardEntry> getLeaderboard() {
        return leaderboard;
    }

}
