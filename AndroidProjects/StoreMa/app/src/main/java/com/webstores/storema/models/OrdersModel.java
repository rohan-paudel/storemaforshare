package com.webstores.storema.models;

import java.util.ArrayList;

public class OrdersModel {

    String orderNumber;
    String date;
    String customerName;
    String customerPhone;
    String customerEmail;
    String customerAddress;
    ArrayList<String> ordersId;
    ArrayList<String> ordersPrice;
    ArrayList<String> ordersQuantity;
    ArrayList<String> ordersName;
    ArrayList<String> ordersImage;

    ArrayList<String> ordersGst;

    String totalOrders;
    String companyPhone;
    String companyName;

    String deliveryCharge;
    String orderStatus;


    public OrdersModel(String orderNumber, String date, String customerName, String customerPhone, String customerEmail, String customerAddress, ArrayList<String> ordersId, ArrayList<String> ordersPrice, ArrayList<String> ordersQuantity, String deliveryCharge, String orderStatus, ArrayList<String> ordersName, ArrayList<String> ordersImage, String totalOrders, ArrayList<String> ordersGst) {
        this.orderNumber = orderNumber;
        this.date = date;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.ordersGst = ordersGst;
        this.ordersId = ordersId;
        this.ordersPrice = ordersPrice;
        this.ordersName = ordersName;
        this.ordersImage = ordersImage;
        this.ordersQuantity = ordersQuantity;
        this.deliveryCharge = deliveryCharge;
        this.orderStatus = orderStatus;
        this.totalOrders = totalOrders;

    }

    public ArrayList<String> getOrdersGst() {
        return ordersGst;
    }

    public void setOrdersGst(ArrayList<String> ordersGst) {
        this.ordersGst = ordersGst;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(String totalOrders) {
        this.totalOrders = totalOrders;
    }

    public ArrayList<String> getOrdersName() {
        return ordersName;
    }

    public void setOrdersName(ArrayList<String> ordersName) {
        this.ordersName = ordersName;
    }

    public ArrayList<String> getOrdersImage() {
        return ordersImage;
    }

    public void setOrdersImage(ArrayList<String> ordersImage) {
        this.ordersImage = ordersImage;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public ArrayList<String> getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(ArrayList<String> ordersId) {
        this.ordersId = ordersId;
    }

    public ArrayList<String> getOrdersPrice() {
        return ordersPrice;
    }

    public void setOrdersPrice(ArrayList<String> ordersPrice) {
        this.ordersPrice = ordersPrice;
    }

    public ArrayList<String> getOrdersQuantity() {
        return ordersQuantity;
    }

    public void setOrdersQuantity(ArrayList<String> ordersQuantity) {
        this.ordersQuantity = ordersQuantity;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
