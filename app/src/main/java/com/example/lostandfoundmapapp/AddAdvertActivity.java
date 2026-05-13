package com.example.lostandfoundmapapp;

// These imports are needed for permissions, images, and navigation
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

// These imports are needed for activity result, permissions, and Room
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

// These imports are needed for location services
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// This activity creates a new lost or found advert
public class AddAdvertActivity extends AppCompatActivity {

    RadioGroup rgPostType;
    EditText etName, etPhone, etDescription, etDate, etLocation;
    Spinner spinnerCategory;
    ImageView ivSelectedImage;
    Button btnChooseImage, btnGetCurrentLocation, btnSave;

    String selectedImageUri = "";

    double selectedLatitude = 0.0;
    double selectedLongitude = 0.0;

    AppDatabase db;
    FusedLocationProviderClient fusedLocationClient;

    // This launcher opens the gallery and stores the selected image URI
    ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Uri imageUri = result.getData().getData();

                    if (imageUri != null) {
                        selectedImageUri = imageUri.toString();
                        ivSelectedImage.setImageURI(imageUri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // This runs when the screen opens
        super.onCreate(savedInstanceState);

        // This connects this Java file to activity_add_advert.xml
        setContentView(R.layout.activity_add_advert);

        // These lines connect Java variables to XML ids
        rgPostType = findViewById(R.id.rgPostType);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation);
        btnSave = findViewById(R.id.btnSave);

        // This creates the Room database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "lost_found_map_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        // This creates the Google location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // This fills the category spinner using strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.category_array,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // This fills the date field with the current date and time
        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        etDate.setText(currentDateTime);

        // This opens the gallery picker
        btnChooseImage.setOnClickListener(v -> {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(pickIntent);
        });

        // This gets the user's current GPS/location coordinates
        btnGetCurrentLocation.setOnClickListener(v -> getCurrentLocation());

        // This saves the advert to the database
        btnSave.setOnClickListener(v -> saveAdvert());
    }

    // This method gets the current device location
    private void getCurrentLocation() {

        // This checks location permission first
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // This requests permission if it has not been granted yet
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100
            );

            return;
        }

        // This gets the last known location
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {

            if (location != null) {

                // These values are saved later for map markers
                selectedLatitude = location.getLatitude();
                selectedLongitude = location.getLongitude();

                // This shows the selected location in the textbox
                etLocation.setText(selectedLatitude + ", " + selectedLongitude);

                Toast.makeText(this, "Current location selected", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Location not available. Set emulator location and try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // This method validates and saves the advert
    private void saveAdvert() {

        // This gets the selected lost/found option
        int selectedTypeId = rgPostType.getCheckedRadioButtonId();

        if (selectedTypeId == -1) {
            Toast.makeText(this, "Please select Lost or Found", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedTypeButton = findViewById(selectedTypeId);

        String postType = selectedTypeButton.getText().toString();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(description)
                || TextUtils.isEmpty(date) || TextUtils.isEmpty(location)) {

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(selectedImageUri)) {
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedLatitude == 0.0 && selectedLongitude == 0.0) {
            Toast.makeText(this, "Please tap GET CURRENT LOCATION before saving", Toast.LENGTH_LONG).show();
            return;
        }

        LostFoundItem item = new LostFoundItem(
                postType,
                name,
                phone,
                description,
                date,
                location,
                category,
                selectedImageUri,
                timestamp,
                selectedLatitude,
                selectedLongitude
        );

        db.lostFoundDao().insert(item);

        Toast.makeText(this, "Advert saved", Toast.LENGTH_SHORT).show();

        finish();
    }
}