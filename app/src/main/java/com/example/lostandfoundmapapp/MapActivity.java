package com.example.lostandfoundmapapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

// This activity displays saved lost/found items on a map
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    AppDatabase db;
    FusedLocationProviderClient fusedLocationClient;

    EditText etRadius;
    Button btnApplyRadius;

    double userLatitude = 0.0;
    double userLongitude = 0.0;
    boolean hasUserLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        etRadius = findViewById(R.id.etRadius);
        btnApplyRadius = findViewById(R.id.btnApplyRadius);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "lost_found_map_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnApplyRadius.setOnClickListener(v -> showItemsOnMap());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // This gives the map a useful starting location
        LatLng deakinBurwood = new LatLng(-37.8476, 145.1149);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deakinBurwood, 13));

        getUserCurrentLocation();

        // This also loads items immediately
        showItemsOnMap();
    }

    // This method gets the user's current location
    private void getUserCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    200
            );

            Toast.makeText(this, "Location permission requested. Press map button again after allowing.", Toast.LENGTH_LONG).show();
            return;
        }

        googleMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {

            if (location != null) {
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();
                hasUserLocation = true;

                LatLng userLatLng = new LatLng(userLatitude, userLongitude);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 13));

                Toast.makeText(this, "Current location loaded", Toast.LENGTH_SHORT).show();

                showItemsOnMap();
            } else {
                hasUserLocation = false;
                Toast.makeText(this, "Current location unavailable. Showing saved items without radius centre.", Toast.LENGTH_LONG).show();
                showItemsOnMap();
            }
        });
    }

    // This method displays saved items and applies radius filtering when possible
    private void showItemsOnMap() {

        if (googleMap == null) {
            return;
        }

        googleMap.clear();

        double radiusKm = 10.0;

        String radiusText = etRadius.getText().toString().trim();

        if (!radiusText.isEmpty()) {
            try {
                radiusKm = Double.parseDouble(radiusText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid radius. Using 10 km.", Toast.LENGTH_SHORT).show();
                radiusKm = 10.0;
            }
        }

        if (hasUserLocation) {
            LatLng userLatLng = new LatLng(userLatitude, userLongitude);

            googleMap.addMarker(new MarkerOptions()
                    .position(userLatLng)
                    .title("Your Current Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        List<LostFoundItem> items = db.lostFoundDao().getAllItems();

        int markerCount = 0;
        LatLng firstItemLocation = null;

        for (LostFoundItem item : items) {

            if (item.latitude == 0.0 && item.longitude == 0.0) {
                continue;
            }

            boolean shouldShow = true;

            // Radius filtering only works when current location is available
            if (hasUserLocation) {
                float[] results = new float[1];

                Location.distanceBetween(
                        userLatitude,
                        userLongitude,
                        item.latitude,
                        item.longitude,
                        results
                );

                double distanceKm = results[0] / 1000.0;
                shouldShow = distanceKm <= radiusKm;
            }

            if (shouldShow) {
                LatLng itemLatLng = new LatLng(item.latitude, item.longitude);

                if (firstItemLocation == null) {
                    firstItemLocation = itemLatLng;
                }

                float markerColour = item.postType.equalsIgnoreCase("Lost")
                        ? BitmapDescriptorFactory.HUE_RED
                        : BitmapDescriptorFactory.HUE_GREEN;

                googleMap.addMarker(new MarkerOptions()
                        .position(itemLatLng)
                        .title(item.postType + " " + item.name)
                        .snippet(item.location)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColour)));

                markerCount++;
            }
        }

        if (firstItemLocation != null && !hasUserLocation) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstItemLocation, 13));
        }

        Toast.makeText(this, markerCount + " item(s) shown on map", Toast.LENGTH_SHORT).show();
    }
}