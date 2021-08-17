package com.thetechboogle.barcode_scanner_app.ui.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.thetechboogle.barcode_scanner_app.databinding.FragmentCameraBinding;

public class CameraFragment extends Fragment {

    private CameraViewModel cameraViewModel;
    private FragmentCameraBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle  savedInstanceState) {
        cameraViewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textDashboard;
        cameraViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}