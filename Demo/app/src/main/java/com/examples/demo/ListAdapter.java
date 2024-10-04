package com.examples.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ListItem> {
    public ListAdapter(Context context, List<ListItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView contentTextView = convertView.findViewById(R.id.contentTextView);

        titleTextView.setText(item.getTitle());
        contentTextView.setText(item.getContent());

        return convertView;
    }
}