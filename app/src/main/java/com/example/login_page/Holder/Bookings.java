package com.example.login_page.Holder;

public class Bookings {
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bookings(String id,String price, String name, String phone, String location, String date) {
        this.price = price;
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.date = date;
        this.id = id;
    }

    public Bookings(){}
    String id;
    String price;
    String name;
    String phone;
    String location;
    String date;

    public String getUserId() {
        return id;
    }

    public void setUserId(String id) {
        this.id = id;
    }


}
