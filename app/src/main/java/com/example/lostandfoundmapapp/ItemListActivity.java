package com.example.lostandfoundmapapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;

// This activity displays all saved lost/found items
public class ItemListActivity extends AppCompatActivity {

    Spinner spinnerFilter;
    ListView listViewItems;

    AppDatabase db;
    ArrayList<LostFoundItem> itemObjects;
    ItemListAdapter itemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        spinnerFilter = findViewById(R.id.spinnerFilter);
        listViewItems = findViewById(R.id.listViewItems);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "lost_found_map_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.filter_array,
                android.R.layout.simple_spinner_item
        );

        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                loadItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        listViewItems.setOnItemClickListener((parent, view, position, id) -> {
            LostFoundItem selectedItem = itemObjects.get(position);

            Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
            intent.putExtra("item_id", selectedItem.id);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        loadItems();
    }

    private void loadItems() {

        String selectedFilter = spinnerFilter.getSelectedItem().toString();

        if (selectedFilter.equals("All")) {
            itemObjects = new ArrayList<>(db.lostFoundDao().getAllItems());
        } else {
            itemObjects = new ArrayList<>(db.lostFoundDao().getItemsByCategory(selectedFilter));
        }

        itemListAdapter = new ItemListAdapter(this, itemObjects);
        listViewItems.setAdapter(itemListAdapter);
    }
}