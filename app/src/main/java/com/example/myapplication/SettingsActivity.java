package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.myapplication.ui.login.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private EditText editTextPassword, editTextRealName, editTextProgram, editTextSchoolEmail;
    private Button buttonConfirm, buttonUpdateProfile, buttonExitAccount;
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
        editTextRealName = findViewById(R.id.editTextRealName);
        editTextProgram = findViewById(R.id.editTextProgram);
        editTextSchoolEmail = findViewById(R.id.editTextSchoolEmail);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);
        buttonExitAccount = findViewById(R.id.buttonExitAccount);

        // Fetch profile data from Firebase
        fetchProfileData();

        // Set up button listeners
        buttonConfirm.setOnClickListener(v -> changePassword());
        buttonUpdateProfile.setOnClickListener(v -> updateProfile());
        buttonExitAccount.setOnClickListener(v -> exitAccount());
    }

    // Fetch profile information from Firebase
    private void fetchProfileData() {
        usersRef.child(username).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check and populate Real Name
                if (dataSnapshot.child("realName").exists()) {
                    String realName = dataSnapshot.child("realName").getValue(String.class);
                    editTextRealName.setText(realName);
                } else {
                    editTextRealName.setText("");  // Leave it blank if not available
                }

                // Check and populate Program
                if (dataSnapshot.child("program").exists()) {
                    String program = dataSnapshot.child("program").getValue(String.class);
                    editTextProgram.setText(program);
                } else {
                    editTextProgram.setText("");  // Leave it blank if not available
                }

                // Check and populate School Email
                if (dataSnapshot.child("schoolEmail").exists()) {
                    String schoolEmail = dataSnapshot.child("schoolEmail").getValue(String.class);
                    editTextSchoolEmail.setText(schoolEmail);
                } else {
                    editTextSchoolEmail.setText("");  // Leave it blank if not available
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SettingsActivity.this, "Failed to fetch profile information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Change the password in Firebase
    private void changePassword() {
        String newPassword = editTextPassword.getText().toString();

        // Validate password length
        if (newPassword.length() <= 5) {
            editTextPassword.setError("Password must be >5 characters");
            return;
        }

        // Update password in Firebase
        usersRef.child(username).child("password").setValue(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingsActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        // Hide the keyboard after successful update
                        hideKeyboard();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                    }
                });

        // Clear the password field
        editTextPassword.setText("");
    }

    // Update the profile information in Firebase
    private void updateProfile() {
        String realName = editTextRealName.getText().toString();
        String program = editTextProgram.getText().toString();
        String schoolEmail = editTextSchoolEmail.getText().toString();

        // Check for empty fields
        if (realName.isEmpty() || program.isEmpty() || schoolEmail.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to hold profile data
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("realName", realName);
        profileData.put("program", program);
        profileData.put("schoolEmail", schoolEmail);

        // Update profile data in Firebase
        usersRef.child(username).child("profile").updateChildren(profileData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        // Hide the keyboard after successful update
                        hideKeyboard();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
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

    // Method to hide the keyboard
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
