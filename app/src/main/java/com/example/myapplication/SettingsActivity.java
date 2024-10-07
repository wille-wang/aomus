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
import com.example.myapplication.util.User;
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
    setContentView(R.layout.activity_settings);

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
    usersRef
        .child(username)
        .addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                  editTextRealName.setText(user.getRealName());
                  editTextProgram.setText(user.getProgram());
                  editTextSchoolEmail.setText(user.getSchoolEmail());
                }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(
                        SettingsActivity.this,
                        "Failed to fetch profile information",
                        Toast.LENGTH_SHORT)
                    .show();
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
    usersRef
        .child(username)
        .child("password")
        .setValue(newPassword)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                Toast.makeText(
                        SettingsActivity.this, "Password updated successfully", Toast.LENGTH_SHORT)
                    .show();
                // Hide the keyboard after successful update
                hideKeyboard();
              } else {
                Toast.makeText(
                        SettingsActivity.this, "Failed to update password", Toast.LENGTH_SHORT)
                    .show();
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

    Map<String, Object> updates = new HashMap<>();

    if (!realName.isEmpty()) {
      updates.put("realName", realName);
    }
    if (!program.isEmpty()) {
      updates.put("program", program);
    }
    if (!schoolEmail.isEmpty()) {
      updates.put("schoolEmail", schoolEmail);
    }

    if (updates.isEmpty()) {
      Toast.makeText(this, "No changes to update", Toast.LENGTH_SHORT).show();
      return;
    }

    usersRef
        .child(username)
        .updateChildren(updates)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                Toast.makeText(
                        SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT)
                    .show();
                hideKeyboard();
              } else {
                Toast.makeText(
                        SettingsActivity.this, "Failed to update profile", Toast.LENGTH_SHORT)
                    .show();
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
