package com.webstores.storema.models;

import java.util.ArrayList;

public class OrdersSubListModel {


    String stringOrderName;
    String stringOrderPrice;
    String stringOrderImage;
    String stringOrderQuantity;

    public OrdersSubListModel(String stringOrderName, String stringOrderPrice, String stringOrderImage, String stringOrderQuantity) {
        this.stringOrderName = stringOrderName;
        this.stringOrderPrice = stringOrderPrice;
        this.stringOrderImage = stringOrderImage;
        this.stringOrderQuantity = stringOrderQuantity;
    }

    public String getStringOrderName() {
        return stringOrderName;
    }

    public void setStringOrderName(String stringOrderName) {
        this.stringOrderName = stringOrderName;
    }

    public String getStringOrderPrice() {
        return stringOrderPrice;
    }

    public void setStringOrderPrice(String stringOrderPrice) {
        this.stringOrderPrice = stringOrderPrice;
    }

    public String getStringOrderImage() {
        return stringOrderImage;
    }

    public void setStringOrderImage(String stringOrderImage) {
        this.stringOrderImage = stringOrderImage;
    }

    public String getStringOrderQuantity() {
        return stringOrderQuantity;
    }

    public void setStringOrderQuantity(String stringOrderQuantity) {
        this.stringOrderQuantity = stringOrderQuantity;
    }
}
