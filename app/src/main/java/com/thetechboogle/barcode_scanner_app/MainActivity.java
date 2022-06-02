package com.thetechboogle.barcode_scanner_app;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.thetechboogle.barcode_scanner_app.databinding.ActivityMainBinding;
import com.thetechboogle.barcode_scanner_app.ui.camera.CameraFragment;
import com.thetechboogle.barcode_scanner_app.ui.home.HomeFragment;
import com.thetechboogle.barcode_scanner_app.ui.inventory.InventoryFragment;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnItemSelectedListener((item) -> {
            Fragment selectedFragment = null;
            switch(item.getItemId()){
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_camera:
                    selectedFragment = new CameraFragment();
                    break;
                case R.id.navigation_inventory:
                    selectedFragment = new InventoryFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, selectedFragment).commit();
            return true;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new HomeFragment()).commit();
    }

};

