package com.example.lostandfoundmapapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

// This activity shows item details and allows deletion
public class ItemDetailActivity extends AppCompatActivity {

    TextView tvTitle, tvDate, tvLocation, tvDescription, tvPhone, tvCategory, tvTimestamp;
    ImageView ivItemImage;
    Button btnRemove;

    AppDatabase db;
    LostFoundItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);
        tvLocation = findViewById(R.id.tvLocation);
        tvDescription = findViewById(R.id.tvDescription);
        tvPhone = findViewById(R.id.tvPhone);
        tvCategory = findViewById(R.id.tvCategory);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        ivItemImage = findViewById(R.id.ivItemImage);
        btnRemove = findViewById(R.id.btnRemove);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "lost_found_map_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        int itemId = getIntent().getIntExtra("item_id", -1);

        if (itemId == -1) {
            Toast.makeText(this, "Invalid item", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        item = db.lostFoundDao().getItemById(itemId);

        if (item == null) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle.setText(item.postType + " " + item.name);
        tvDate.setText(item.date);
        tvLocation.setText(item.location);
        tvDescription.setText(item.description);
        tvPhone.setText(item.phone);
        tvCategory.setText(item.category);
        tvTimestamp.setText(item.timestamp);

        // This avoids gallery URI permission crashes while still showing image attachment area
        ivItemImage.setImageResource(android.R.drawable.ic_menu_gallery);

        btnRemove.setOnClickListener(v -> {
            db.lostFoundDao().delete(item);
            Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}