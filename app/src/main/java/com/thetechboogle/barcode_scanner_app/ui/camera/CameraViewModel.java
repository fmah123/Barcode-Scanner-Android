package com.thetechboogle.barcode_scanner_app.ui.camera;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.mlkit.vision.barcode.common.Barcode;

public class CameraViewModel extends ViewModel {

    private MutableLiveData<Barcode> mBarcodeData;

    public CameraViewModel() {
        mBarcodeData = new MutableLiveData<>();
    }

    public void setBarcodeData(Barcode barcode){
        mBarcodeData.setValue(barcode);
    }

    public LiveData<Barcode> getBarcodeData() {
        return mBarcodeData;
    }
}