package com.example.bloodcamp.Views;

public class Post {
    private String bloodCampName;
    private String organizerName;
    private String description;
    private String location;

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    private String postedDate;

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    private Vote vote;

    public Post(String bloodCampName, String organizerName, String description, String location,Vote vote,String postedDate) {
        this.bloodCampName = bloodCampName;
        this.organizerName = organizerName;
        this.description = description;
        this.location = location;
        this.vote = vote;
        this.postedDate = postedDate;
    }

    public Post() {
    }


    public String getBloodCampName() {
        return bloodCampName;
    }

    public void setBloodCampName(String bloodCampName) {
        this.bloodCampName = bloodCampName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
