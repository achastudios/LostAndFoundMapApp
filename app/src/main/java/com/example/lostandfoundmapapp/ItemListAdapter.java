package com.example.lostandfoundmapapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

// This adapter controls how each lost/found item appears in the list
public class ItemListAdapter extends ArrayAdapter<LostFoundItem> {

    Context context;
    List<LostFoundItem> items;

    public ItemListAdapter(Context context, List<LostFoundItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        }

        TextView tvRowTitle = convertView.findViewById(R.id.tvRowTitle);
        TextView tvRowSubtitle = convertView.findViewById(R.id.tvRowSubtitle);

        LostFoundItem item = items.get(position);

        tvRowTitle.setText(item.postType + " " + item.name);
        tvRowSubtitle.setText(item.location + " • " + item.date);

        return convertView;
    }
}