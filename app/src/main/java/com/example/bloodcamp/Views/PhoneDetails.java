package com.example.bloodcamp.Views;

public class PhoneDetails {
    private String member;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    private String imageUri;
    public PhoneDetails(String id,String uploadTime,String member, String phone, String battery, String camera, String ram, String storage, String fingerPrint, String connection, String description, double price,String imageUri) {
        this.member = member;
        this.id = id;
        this.uploadTime = uploadTime;
        this.phone = phone;
        this.battery = battery;
        this.camera = camera;
        this.ram = ram;
        this.storage = storage;
        this.fingerPrint = fingerPrint;
        this.connection = connection;
        this.description = description;
        this.price = price;
        this.imageUri = imageUri;
    }

    public PhoneDetails()
    {

    }
    private String phone;

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private String battery;
    private String camera;
    private String ram;
    private String storage;
    private String fingerPrint;
    private String connection;
    private String description;
    private double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    private String id;
    private String uploadTime;
}
