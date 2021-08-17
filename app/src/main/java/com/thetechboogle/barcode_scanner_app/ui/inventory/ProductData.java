package com.thetechboogle.barcode_scanner_app.ui.inventory;

import java.util.Date;

class ProductData {

    //model class
    private String ItemName;
    private int ItemQty;
    private String ExpiryDate;

    public ProductData(String itemName, int itemQty, String expiryDate) {
        this.ItemName = itemName;
        this.ItemQty = itemQty;
        this.ExpiryDate = expiryDate;
    }

    public String getItemName() {
        return ItemName;
    }

    public int getItemQty() {
        return ItemQty;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

}
