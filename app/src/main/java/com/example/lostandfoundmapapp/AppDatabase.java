package com.example.lostandfoundmapapp;

// These imports are needed for Room database setup
import androidx.room.Database;
import androidx.room.RoomDatabase;

// This class defines the Room database
@Database(entities = {LostFoundItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // This provides access to the DAO methods
    public abstract LostFoundDao lostFoundDao();
}