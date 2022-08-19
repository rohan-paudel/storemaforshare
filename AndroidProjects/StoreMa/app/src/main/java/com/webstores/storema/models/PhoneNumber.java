package com.webstores.storema.models;

public class PhoneNumber {

    private static String phoneNumber;
    private static String companyName;


    public static void setPhoneNumber(String phoneNumber) {
        PhoneNumber.phoneNumber = phoneNumber;
    }

    public static String getCompanyName() {
        return companyName;
    }

    public static void setCompanyName(String companyName) {
        PhoneNumber.companyName = companyName;
    }

    public PhoneNumber(String phoneNumber, String companyName){
        PhoneNumber.phoneNumber = phoneNumber;
        PhoneNumber.companyName = companyName;
    }


    public static String getPhoneNumber() {
        return phoneNumber;
    }
}
