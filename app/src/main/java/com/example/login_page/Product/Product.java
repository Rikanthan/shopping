package com.example.login_page.Product;

public class Product {
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCatergory() {
        return Catergory;
    }

    public void setCatergory(String catergory) {
        Catergory = catergory;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    private String productName;
    private String Catergory;
    private String Quantity;
    private int Price;

    public Product() {
    }


}
