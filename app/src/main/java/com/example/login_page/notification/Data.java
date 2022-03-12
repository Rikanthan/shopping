package com.example.login_page.notification;

public class Data {
    private String Title;
    private String Message;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    private String Date;
    private String Status;

    public Data(String title, String message, String date, String status) {
        Title = title;
        Message = message;
        Date = date;
        Status = status;
    }

    public Data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}
