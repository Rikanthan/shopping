package com.example.login_page.Images;

public class Upload{
    private String mName;
    private String mImageUrl;
    public Upload()
    {

    }
    public Upload(String name, String imageUrl)
    {
        if(name.trim().equals(""))
        {
            name="No name";
        }
        mImageUrl="https://firebasestorage.googleapis.com/v0/b/login-page-7e049.appspot.com/o?name=uploads%2F1603001360565.jpg&uploadType=resumable&upload_id=ABg5-Uxz8hUTlisz5eSmrufHpRkr6jdrRWY_8KxbQrt9tSjRGCsZtkhPYHMey-vOAOUCOcqvg0WoYliOeX4kD4fjv0A&upload_protocol=resumable";
        mName=name;
       // mImageUrl=imageUrl;
    }




    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }



    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }




}