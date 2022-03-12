package com.example.login_page.Images;

public class Upload{
    private String mName;
    private String mImageUrl;
    private String mPrice;
    private String mQuantity;
    private String mCatergory;
    private String mCatergoryId;
    private String muploadId;



    public Upload(String name, String imageUrl, String price, String quantity, String catergory, String uploadId, String catergoryId)
    {
        if(name.trim().equals(""))
        {
            name="No name";
        }
        mImageUrl  =  imageUrl;
        mName  =  name;
        mPrice  =  price;
        mQuantity  =  quantity;
        mCatergory  = catergory;
        muploadId  =  uploadId;
        mCatergoryId = catergoryId;
    }

    public Upload()
    {

    }


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

    public String getmCatergory() {
        return mCatergory;
    }

    public void setmCatergory(String mCatergory) {
        this.mCatergory = mCatergory;
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

    public String getmuploadId() {
        return muploadId;
    }

    public void setmuploadId(String muploadId) {
        this.muploadId = muploadId;
    }
    public String getmCatergoryId() {
        return mCatergoryId;
    }

    public void setmCatergoryId(String mCatergoryId) {
        this.mCatergoryId = mCatergoryId;
    }

}