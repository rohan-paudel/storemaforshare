package com.webstores.storema.models;

public class ItemsModel {

    String itemId;
    String itemName;
    String itemUnit;
    String itemImage1;
    String itemImage2;
    String itemImage3;
    String itemImage4;
    String itemImage5;

    String isImage1;
    String isImage2;
    String isImage3;
    String isImage4;
    String isImage5;


    String isDiscountInPercentage;
    String discount;
    String gst;
    String mrp;
    String category;
    String itemCategoryPositionNumber;
    String piecesUnitPositionNumber;
    String extraField1Name;
    String extraField1Value;
    String extraField2Name;
    String extraField2Value;
    String extraField3Name;
    String extraField3Value;
    String description;
    String isItemActive;
    String numberOfExtraFields;


    public ItemsModel(String itemId, String itemName, String itemUnit, String itemImage1, String itemImage2, String itemImage3, String itemImage4, String itemImage5, String isImage1, String isImage2, String isImage3, String isImage4, String isImage5, String isDiscountInPercentage, String discount, String gst, String mrp, String category, String itemCategoryPositionNumber, String piecesUnitPositionNumber, String extraField1Name, String extraField1Value, String extraField2Name, String extraField2Value, String extraField3Name, String extraField3Value, String description, String isItemActive, String numberOfExtraFields) {

        this.itemId = itemId;
        this.itemName = itemName;
        this.itemUnit = itemUnit;
        this.itemImage1 = itemImage1;
        this.itemImage2 = itemImage2;
        this.itemImage3 = itemImage3;
        this.itemImage4 = itemImage4;
        this.itemImage5 = itemImage5;
        this.isImage1 = isImage1;
        this.isImage2 = isImage2;
        this.isImage3 = isImage3;
        this.isImage4 = isImage4;
        this.isImage5 = isImage5;
        this.isDiscountInPercentage = isDiscountInPercentage;
        this.discount = discount;
        this.gst = gst;
        this.mrp = mrp;
        this.category = category;
        this.itemCategoryPositionNumber = itemCategoryPositionNumber;
        this.piecesUnitPositionNumber = piecesUnitPositionNumber;
        this.extraField1Name = extraField1Name;
        this.extraField1Value = extraField1Value;
        this.extraField2Name = extraField2Name;
        this.extraField2Value = extraField2Value;
        this.extraField3Name = extraField3Name;
        this.extraField3Value = extraField3Value;
        this.description = description;
        this.isItemActive = isItemActive;
        this.numberOfExtraFields = numberOfExtraFields;
    }



    public String getNumberOfExtraFields() {
        return numberOfExtraFields;
    }

    public void setNumberOfExtraFields(String numberOfExtraFields) {
        this.numberOfExtraFields = numberOfExtraFields;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getItemImage1() {
        return itemImage1;
    }

    public void setItemImage1(String itemImage1) {
        this.itemImage1 = itemImage1;
    }

    public String getItemImage2() {
        return itemImage2;
    }

    public void setItemImage2(String itemImage2) {
        this.itemImage2 = itemImage2;
    }

    public String getItemImage3() {
        return itemImage3;
    }

    public void setItemImage3(String itemImage3) {
        this.itemImage3 = itemImage3;
    }

    public String getItemImage4() {
        return itemImage4;
    }

    public void setItemImage4(String itemImage4) {
        this.itemImage4 = itemImage4;
    }

    public String getItemImage5() {
        return itemImage5;
    }

    public void setItemImage5(String itemImage5) {
        this.itemImage5 = itemImage5;
    }

    public String getIsImage1() {
        return isImage1;
    }

    public void setIsImage1(String isImage1) {
        this.isImage1 = isImage1;
    }

    public String getIsImage2() {
        return isImage2;
    }

    public void setIsImage2(String isImage2) {
        this.isImage2 = isImage2;
    }

    public String getIsImage3() {
        return isImage3;
    }

    public void setIsImage3(String isImage3) {
        this.isImage3 = isImage3;
    }

    public String getIsImage4() {
        return isImage4;
    }

    public void setIsImage4(String isImage4) {
        this.isImage4 = isImage4;
    }

    public String getIsImage5() {
        return isImage5;
    }

    public void setIsImage5(String isImage5) {
        this.isImage5 = isImage5;
    }

    public String getIsDiscountInPercentage() {
        return isDiscountInPercentage;
    }

    public void setIsDiscountInPercentage(String isDiscountInPercentage) {
        this.isDiscountInPercentage = isDiscountInPercentage;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemCategoryPositionNumber() {
        return itemCategoryPositionNumber;
    }

    public void setItemCategoryPositionNumber(String itemCategoryPositionNumber) {
        this.itemCategoryPositionNumber = itemCategoryPositionNumber;
    }

    public String getPiecesUnitPositionNumber() {
        return piecesUnitPositionNumber;
    }

    public void setPiecesUnitPositionNumber(String piecesUnitPositionNumber) {
        this.piecesUnitPositionNumber = piecesUnitPositionNumber;
    }

    public String getExtraField1Name() {
        return extraField1Name;
    }

    public void setExtraField1Name(String extraField1Name) {
        this.extraField1Name = extraField1Name;
    }

    public String getExtraField1Value() {
        return extraField1Value;
    }

    public void setExtraField1Value(String extraField1Value) {
        this.extraField1Value = extraField1Value;
    }

    public String getExtraField2Name() {
        return extraField2Name;
    }

    public void setExtraField2Name(String extraField2Name) {
        this.extraField2Name = extraField2Name;
    }

    public String getExtraField2Value() {
        return extraField2Value;
    }

    public void setExtraField2Value(String extraField2Value) {
        this.extraField2Value = extraField2Value;
    }

    public String getExtraField3Name() {
        return extraField3Name;
    }

    public void setExtraField3Name(String extraField3Name) {
        this.extraField3Name = extraField3Name;
    }

    public String getExtraField3Value() {
        return extraField3Value;
    }

    public void setExtraField3Value(String extraField3Value) {
        this.extraField3Value = extraField3Value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsItemActive() {
        return isItemActive;
    }

    public void setIsItemActive(String isItemActive) {
        this.isItemActive = isItemActive;
    }
}
