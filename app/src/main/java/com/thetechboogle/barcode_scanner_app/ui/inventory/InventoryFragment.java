package com.thetechboogle.barcode_scanner_app.ui.inventory;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thetechboogle.barcode_scanner_app.databinding.FragmentInventoryBinding;

import java.util.ArrayList;

public class InventoryFragment extends Fragment {

    private InventoryViewModel inventoryViewModel;
    private FragmentInventoryBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        binding = FragmentInventoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView recyclerView = binding.ProductItemRecyclerView;
        inventoryViewModel.getProductItemList().observe(getViewLifecycleOwner(), new Observer<ArrayList<ProductData>>() {
            @Override
            public void onChanged(ArrayList<ProductData> productData) {
                InitialiseProductDataRecyclerView(recyclerView, productData);
            }
        });

        return root;
    }



    public void InitialiseProductDataRecyclerView(RecyclerView recyclerView, ArrayList<ProductData> productDataList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new DataAdapter(productDataList,getActivity()));
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}