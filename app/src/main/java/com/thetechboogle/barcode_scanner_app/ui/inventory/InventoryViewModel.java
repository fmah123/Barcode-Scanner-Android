package com.thetechboogle.barcode_scanner_app.ui.inventory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.thetechboogle.barcode_scanner_app.ui.camera.CameraViewModel;

import java.util.ArrayList;

public class InventoryViewModel extends ViewModel  {


    private MutableLiveData<ArrayList<ProductData>> mProducts;
    ArrayList<ProductData> productData;

    public InventoryViewModel() {
        mProducts = new MutableLiveData<>();
        init();
    }



    public LiveData<ArrayList<ProductData>> getProductItemList() {
        return mProducts;
    }

    public void init(){
        addProductData();
        mProducts.setValue(productData);
    }

    private void addProductData(){
        productData = new ArrayList<>();
        productData.add(new ProductData("Sprite", 24, "11/07/2022"));
    }

}