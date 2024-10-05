package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.myapplication.ui.login.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private EditText editTextPassword;
    private Button buttonConfirm;
    private Button buttonExitAccount;
    private DatabaseReference usersRef;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.tool_bar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Get the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        // Initialize views
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonExitAccount = findViewById(R.id.buttonExitAccount);

        // Set up button listeners
        buttonConfirm.setOnClickListener(v -> changePassword());
        buttonExitAccount.setOnClickListener(v -> exitAccount());
    }

    // Change the password in Firebase
    private void changePassword() {
        String newPassword = editTextPassword.getText().toString();

        // Validate password length directly in this method
        if (newPassword.length() <= 5) {
            editTextPassword.setError("Password must be >5 characters");
            return;
        }

        // Update password in Firebase
        usersRef.child(username).child("password").setValue(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingsActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                    }
                });

        // Clear the password field
        editTextPassword.setText("");
    }

    private void exitAccount() {
        // Clear login status
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
