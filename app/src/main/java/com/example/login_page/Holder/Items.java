package com.example.login_page.Holder;

public class Items {
    public Items(String pimageurl, String pcategory, String pquantity, String pname, String pprice) {
        this.pimageurl = pimageurl;
        this.pcategory = pcategory;
        this.pquantity = pquantity;
        this.pname = pname;
        this.pprice = pprice;
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

    public void setPimageurl(String pimageurl) {
        this.pimageurl = pimageurl;
    }

    public String getPcategory() {
        return pcategory;
    }

    public void setPcategory(String pcategory) {
        this.pcategory = pcategory;
    }

    public String getPquantity() {
        return pquantity;
    }

    public void setPquantity(String pquantity) {
        this.pquantity = pquantity;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    private String pprice;


}
