package com.example.login_page.Images;

public class Upload{
    private String mName;
    private String mImageUrl;

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(String mQuantity) {
        this.mQuantity = mQuantity;
    }

    private String mPrice;
    private String mQuantity;

    public String getmCatergory() {
        return mCatergory;
    }

    public void setmCatergory(String mCatergory) {
        this.mCatergory = mCatergory;
    }

    private String mCatergory;
    public Upload()
    {

    }
    public Upload(String name, String imageUrl,String price,String quantity,String catergory)
    {
        if(name.trim().equals(""))
        {
            name="No name";
        }
        mImageUrl="https://firebasestorage.googleapis.com/v0/b/login-page-7e049.appspot.com/o?name=uploads%2F1603001360565.jpg&uploadType=resumable&upload_id=ABg5-Uxz8hUTlisz5eSmrufHpRkr6jdrRWY_8KxbQrt9tSjRGCsZtkhPYHMey-vOAOUCOcqvg0WoYliOeX4kD4fjv0A&upload_protocol=resumable";
        mName=name;
        mPrice=price;
        mQuantity=quantity;
        mCatergory=catergory;


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