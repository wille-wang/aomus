package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.tool_bar_settings);
        setSupportActionBar(toolbar);

        // Set the toolbar title to an empty string
        getSupportActionBar().setTitle("");

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set title (optional)
        getSupportActionBar().setTitle("Back");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
