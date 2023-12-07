package com.example.clp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.MultiDex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Context;

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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    ActivityMainBinding binding;
    ImageView iv;
    EditText editText;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private com.naver.maps.map.MapFragment mapFragment;
    private static NaverMap naverMap;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new MapFragment());
        iv = findViewById(R.id.iv);
        editText = findViewById(R.id.editText);

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(36.167910, 128.467695), // 대상 지점
                16, // 줌 레벨
                20, // 기울임 각도
                0 // 베어링 각도
        );
        NaverMapOptions options = new NaverMapOptions()
                .camera(cameraPosition)
                .mapType(NaverMap.MapType.Terrain)
                .enabledLayerGroups(NaverMap.LAYER_GROUP_BUILDING)
                .compassEnabled(true)
                .scaleBarEnabled(true)
                .locationButtonEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        com.naver.maps.map.MapFragment mapFragment = (com.naver.maps.map.MapFragment) fm.findFragmentById(R.id.frame_layout);
        if (mapFragment == null) {
            mapFragment = com.naver.maps.map.MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.frame_layout, mapFragment).commit();
        }
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        //바텀 네비게이션
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);

        Marker marker1 = new Marker();
        marker1.setPosition(new LatLng(36.167840, 128.465839));
        marker1.setMap(naverMap);

        InfoWindow infoWindow1 = new InfoWindow();
        infoWindow1.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {return "문수지";}
        });
        infoWindow1.open(marker1);

        // 마커2
        Marker marker2 = new Marker();
        marker2.setPosition(new LatLng(36.167910, 128.467695));
        marker2.setMap(naverMap);

        InfoWindow infoWindow2 = new InfoWindow();
        infoWindow2.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "2호관";
            }
        });
        infoWindow2.open(marker2);

        // 마커3
        Marker marker3 = new Marker();
        marker3.setPosition(new LatLng(36.170820, 128.467438));
        marker3.setMap(naverMap);

        InfoWindow infoWindow3 = new InfoWindow();
        infoWindow3.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "벽강아트센터";
            }
        });
        infoWindow3.open(marker3);

        infoWindow1.close();
        infoWindow2.close();
        infoWindow3.close();

        // 지도를 클릭하면 정보 창을 닫음
        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow1.close();
            infoWindow2.close();
            infoWindow3.close();
        });

        Overlay.OnClickListener markerClickListener = overlay -> {
            if (overlay instanceof Marker) {
                Marker clickedMarker = (Marker) overlay;

                // 현재 마커에 연결된 InfoWindow 가져오기
                InfoWindow associatedInfoWindow = clickedMarker.getInfoWindow();

                if (associatedInfoWindow != null && associatedInfoWindow.getMap() != null) {
                    // 이미 열려있는 경우 닫음
                    associatedInfoWindow.close();
                } else {
                    // 열려있지 않은 경우 해당 마커의 위치에 맞는 InfoWindow 열기
                    if (clickedMarker.equals(marker1)) {
                        infoWindow1.open(clickedMarker);
                        infoWindow2.close();
                        infoWindow3.close();
                    } else if (clickedMarker.equals(marker2)) {
                        infoWindow2.open(clickedMarker);
                        infoWindow1.close();
                        infoWindow3.close();
                    } else if (clickedMarker.equals(marker3)) {
                        infoWindow3.open(clickedMarker);
                        infoWindow1.close();
                        infoWindow2.close();
                    }
                }
            }

            return true;
        };
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        marker1.setOnClickListener(markerClickListener);
        marker2.setOnClickListener(markerClickListener);
        marker3.setOnClickListener(markerClickListener);
    }

    private  void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}
