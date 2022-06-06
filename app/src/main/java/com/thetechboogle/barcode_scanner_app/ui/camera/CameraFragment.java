package com.thetechboogle.barcode_scanner_app.ui.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.thetechboogle.barcode_scanner_app.R;
import com.thetechboogle.barcode_scanner_app.databinding.FragmentCameraBinding;
import com.thetechboogle.barcode_scanner_app.databinding.FragmentHomeBinding;
import com.thetechboogle.barcode_scanner_app.ui.inventory.InventoryFragment;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraFragment extends Fragment {

    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private CameraViewModel cameraViewModel;
    private FragmentCameraBinding binding;
    private PreviewView previewCamera;
    private ListenableFuture cameraProviderFuture;
    private ExecutorService cameraExecutor;
    private ExecutorService cameraExecutor1;
    private ImageAnalyser analyser;
    private Context safeContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        safeContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle  savedInstanceState) {
        /*This line of code connects the view model to the fragment*/

        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cameraViewModel = new ViewModelProvider(this).get(CameraViewModel.class);

        previewCamera = binding.previewView;


        return root;
    }

    private boolean hasCameraPermissions(){
        return ContextCompat.checkSelfPermission(safeContext, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionLauncher(){
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), (isGranted) -> {
            if (isGranted){
                Log.d("permission", "Camera permission is allowed to be accessed: " + isGranted);
                startCamera();
            }else {
                Log.d("permission", "Camera permission is allowed to be accessed: " + isGranted);
                Toast.makeText(safeContext, "Change Permissions to access camera", Toast.LENGTH_SHORT).show();

            }
        }).launch(Manifest.permission.CAMERA);

    }

    public void startCamera(){
        Log.d("permission", "starting camera");
        cameraExecutor = Executors.newSingleThreadExecutor();
        cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext);

        analyser = new ImageAnalyser();

        cameraProviderFuture.addListener(() -> {
            try{
                ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                bindingPreview(processCameraProvider);

            } catch (ExecutionException | InterruptedException e){
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(safeContext));
    }

    public void takePhoto(){
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(hasCameraPermissions()){
            startCamera();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Log.d("permission", "Showing rationale on UI");
        } else {
            requestPermissionLauncher();
        }

        cameraExecutor1 = Executors.newSingleThreadExecutor();
    }



    private void bindingPreview(ProcessCameraProvider processCameraProvider) {
        Log.d("permission", "bindingPreview() Called");
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cameraExecutor1, previewCamera.getSurfaceProvider());
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        ImageCapture imageCapture = new ImageCapture.Builder().build();


        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(cameraExecutor, analyser);

        processCameraProvider.unbindAll();

        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);

    }

    public class ImageAnalyser implements ImageAnalysis.Analyzer {
        @Override
        public void analyze(@NonNull ImageProxy image) {
            scanBarcode(image);
        }
    }

    private void scanBarcode(ImageProxy image) {
        Log.d("permission", "scanBarcode() called");
        @SuppressLint("UnsafeOptInUsageError") Image cameraRoll = image.getImage();
        if(cameraRoll != null){
            InputImage inputImage = InputImage.fromMediaImage(cameraRoll, image.getImageInfo().getRotationDegrees());
            BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                    .build();

            BarcodeScanner scanner = BarcodeScanning.getClient(options);
            Task<List<Barcode>> result = scanner.process(inputImage)
                    .addOnSuccessListener(this::barcodeDataReader)
                    .addOnFailureListener(Throwable::printStackTrace)
                    .addOnCompleteListener((task) -> {
                        image.close();
                    });

        }
    }

    private void barcodeDataReader(List<Barcode> barcodes) {

        Log.d("permission", "barcodeDataReader() called");
        for (Barcode barcode: barcodes) {
            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            String rawValue = barcode.getRawValue();

            Log.d("barcode", "rawValue = " + rawValue);
            int valueType = barcode.getValueType();
            // See API reference for complete list of supported types
            if (valueType == Barcode.TYPE_PRODUCT) {
                Log.d("barcode", "Inside if statement");
                String var1 = barcode.getDisplayValue();

                Log.d("barcode", "var1 = " + var1);
                Bundle bundle = new Bundle();
             git   bundle.putString("product",var1);
                getParentFragmentManager().setFragmentResult("Barcode data", bundle);
                //getParentFragmentManager().beginTransaction().replace(R.id.navigation_camera, new InventoryFragment()).commit();

            } else {
                Log.d("barcode", "Barcode not read!! Check now");
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}