package com.example.login_page.Product;

public class Cart {
    public Cart(String pname, Long price, Long quantity) {
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
    }

    String pname;
    Long price;
    Long quantity;

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
