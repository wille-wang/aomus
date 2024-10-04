package com.examples.demo;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TwoActivity extends AppCompatActivity {
    private List<ListItem> listItems;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        // 设置标题
        TextView recentTextView = findViewById(R.id.recentTextView);
        recentTextView.setText("Recent");

        // 初始化数据
        listItems = new ArrayList<>();
        listItems.add(new ListItem("Old Art", "The University of Melbourne, Old Arts Building, College Cres, Parkville VIC 3052"));
        listItems.add(new ListItem("Old Art", "The University of Melbourne, Old Arts Building, College Cres, Parkville VIC 3052"));
        listItems.add(new ListItem("Old Art", "The University of Melbourne, Old Arts Building, College Cres, Parkville VIC 3052"));

        // 设置ListView适配器
        ListView listView = findViewById(R.id.listView);
        adapter = new ListAdapter(this, listItems);
        listView.setAdapter(adapter);
    }
}