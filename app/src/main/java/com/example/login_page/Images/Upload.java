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
        mImageUrl=imageUrl;
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