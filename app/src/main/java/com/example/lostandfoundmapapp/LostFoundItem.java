package com.example.lostandfoundmapapp;

// These imports are needed for Room database annotations
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// This class represents one lost or found item in the Room database
@Entity(tableName = "lost_found_items")
public class LostFoundItem {

    // This is the auto-generated primary key
    @PrimaryKey(autoGenerate = true)
    public int id;

    // These fields store item information
    public String postType;
    public String name;
    public String phone;
    public String description;
    public String date;
    public String location;
    public String category;
    public String imageUri;
    public String timestamp;

    // These fields store map location information
    public double latitude;
    public double longitude;

    // This constructor creates one saved lost/found item
    public LostFoundItem(String postType, String name, String phone, String description,
                         String date, String location, String category, String imageUri,
                         String timestamp, double latitude, double longitude) {

        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.category = category;
        this.imageUri = imageUri;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}