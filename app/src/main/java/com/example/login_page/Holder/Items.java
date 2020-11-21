package com.example.login_page.Holder;

public class Items {
    public Items(String imageurl, String category, String quantity, String name, String price) {
        this.pimageurl = imageurl;
        this.pcategory = category;
        this.pquantity = quantity;
        this.pname = name;
        this.pprice = price;
    }
    public Items()
    {

    }

    private String pimageurl;
    private String pcategory;
    private String pquantity;
    private String pname;

    public String getPimageurl() {
        return pimageurl;
    }

    public void setPimageurl(String imageurl) {
        this.pimageurl = imageurl;
    }

    public String getPcategory() {
        return pcategory;
    }

    public void setPcategory(String category) {
        this.pcategory = category;
    }

    public String getPquantity() {
        return pquantity;
    }

    public void setPquantity(String quantity) {
        this.pquantity = quantity;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String name) {
        this.pname = name;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String price) {
        this.pprice = price;
    }

    private String pprice;


}
