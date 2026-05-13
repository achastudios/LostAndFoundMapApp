package com.example.lostandfoundmapapp;

// These imports are needed for Room queries
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// This interface contains database actions
@Dao
public interface LostFoundDao {

    // This inserts one item into the database
    @Insert
    void insert(LostFoundItem item);

    // This returns all saved items
    @Query("SELECT * FROM lost_found_items ORDER BY id DESC")
    List<LostFoundItem> getAllItems();

    // This returns saved items by category
    @Query("SELECT * FROM lost_found_items WHERE category = :category ORDER BY id DESC")
    List<LostFoundItem> getItemsByCategory(String category);

    // This returns one item by its database id
    @Query("SELECT * FROM lost_found_items WHERE id = :id LIMIT 1")
    LostFoundItem getItemById(int id);

    // This deletes one item
    @Delete
    void delete(LostFoundItem item);
}