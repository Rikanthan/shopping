package com.example.login_page;

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

    public String getTypequantity() {
        return Typequantity;
    }

    public void setTypequantity(String typequantity) {
        Typequantity = typequantity;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
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
    private int Quantity;
    private int Price;
    private String Typequantity;

    public Product() {
    }


}
