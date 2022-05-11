package com.example.bloodcamp.Views;

public class Member {
    private String Name;
    private String Email;
    private long Mobile;
    private  String Location;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public Member(String name, String email, long mobile, String location, String userType, String password) {
        Name = name;
        Email = email;
        Mobile = mobile;
        Location = location;
        this.userType = userType;
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    private String userType;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public long getMobile() {
        return Mobile;
    }

    public void setMobile(long mobile) {
        Mobile = mobile;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }


    public Member()
    {

    }
}
