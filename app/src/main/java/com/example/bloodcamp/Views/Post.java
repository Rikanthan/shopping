package com.example.bloodcamp.Views;

public class Post {
    private String bloodCampName;
    private String organizerName;
    private String description;
    private String location;
    private String postId;

    public Post(String bloodCampName, String organizerName, String description, String location, String postId, String bloodCampId, String imageUri, String postedDate, Vote vote) {
        this.bloodCampName = bloodCampName;
        this.organizerName = organizerName;
        this.description = description;
        this.location = location;
        this.postId = postId;
        this.bloodCampId = bloodCampId;
        this.imageUri = imageUri;
        this.postedDate = postedDate;
        this.vote = vote;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getBloodCampId() {
        return bloodCampId;
    }

    public void setBloodCampId(String bloodCampId) {
        this.bloodCampId = bloodCampId;
    }

    private String bloodCampId;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    private String imageUri;

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
