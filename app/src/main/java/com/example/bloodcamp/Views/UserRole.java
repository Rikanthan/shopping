package com.example.bloodcamp.Views;

public class UserRole {
    public UserRole(String uid, String name, String userRole) {
        this.uid = uid;
        this.name = name;
        this.userRole = userRole;
    }

    private String uid;
    private String name;
    private String userRole;

    public UserRole() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
