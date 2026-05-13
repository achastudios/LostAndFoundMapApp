package com.example.lostandfoundmapapp;

// These imports are needed for screen navigation
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

// This import is needed for AppCompatActivity
import androidx.appcompat.app.AppCompatActivity;

// This is the home screen activity
public class MainActivity extends AppCompatActivity {

    // These variables connect to the home screen buttons
    Button btnCreateAdvert;
    Button btnShowItems;
    Button btnShowMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // This runs when the activity opens
        super.onCreate(savedInstanceState);

        // This connects this Java file to activity_main.xml
        setContentView(R.layout.activity_main);

        // These lines connect Java variables to XML ids
        btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        btnShowItems = findViewById(R.id.btnShowItems);
        btnShowMap = findViewById(R.id.btnShowMap);

        // This opens the create advert screen
        btnCreateAdvert.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAdvertActivity.class);
            startActivity(intent);
        });

        // This opens the list screen
        btnShowItems.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
            startActivity(intent);
        });

        // This opens the map screen
        btnShowMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });
    }
}