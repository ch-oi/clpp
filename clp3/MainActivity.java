package com.example.clp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.MultiDex;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.clp3.databinding.ActivityMainBinding;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ImageView iv;
    Uri imgUri;
    EditText editText;
    Button cu, upload;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new MapFragment());
        iv = findViewById(R.id.iv);
        editText = findViewById(R.id.editText);


        //바텀 네비게이션
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new MapFragment());
            } else if (itemId == R.id.add) {
                replaceFragment(new AddFragment());
            } else if (itemId == R.id.account) {
                replaceFragment(new AccountFragment());
            } else if (itemId == R.id.image) {
                replaceFragment(new ImageFragment());
            }

            return true;
        });


    }

    private  void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


}