package com.example.login_page.Product;

import android.os.Parcelable;

import java.io.Serializable;

public class Cart implements Serializable {
    public Cart(String pname, Long price, Long quantity,String imageUrl,String productId) {
        this.productId = productId;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    String pname;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String imageUrl;
    Long price;
    Long quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    String productId;

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public Cart()
    {

    }
}
