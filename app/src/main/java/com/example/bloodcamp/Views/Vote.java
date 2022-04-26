package com.example.bloodcamp.Views;

import java.util.List;

public class Vote {
    private List<String> votedPeople;
    private int totalVote = 0;
    private int interestedVote = 0;
    private int attendVote = 0;
    private int notAttendVote = 0;

    public Vote(List<String> votedPeople, int totalVote, int interestedVote, int attendVote, int notAttendVote) {
        this.votedPeople = votedPeople;
        this.totalVote = totalVote;
        this.interestedVote = interestedVote;
        this.attendVote = attendVote;
        this.notAttendVote = notAttendVote;
    }

    public Vote() {
    }

    public List<String> getVotedPeople() {
        return votedPeople;
    }

    public void setVotedPeople(List<String> votedPeople) {
        this.votedPeople = votedPeople;
    }

    public int getTotalVote() {
        return totalVote;
    }

    public int getInterestedVote() {
        return interestedVote;
    }

    public void setInterestedVote() {
        this.interestedVote++;
        this.totalVote++;
    }

    public int getAttendVote() {
        return attendVote;
    }

    public void setAttendVote() {
        this.attendVote++;
        this.totalVote++;
    }

    public int getNotAttendVote() {
        return notAttendVote;
    }

    public void setNotAttendVote() {
        this.notAttendVote++;
        this.totalVote++;
    }
}
