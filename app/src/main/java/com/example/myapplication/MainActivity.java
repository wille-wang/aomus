package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static class User {
        private String id;
        private String username;
        private String email;

        public User(String username, String email) {
            this.id = UUID.randomUUID().toString();
            this.username = username;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

    }

    // declare database variables
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the database
        db = FirebaseDatabase.getInstance();
    }

    // click event for the Submit button
    public void submit(View v) {
        // instantiate a new User object
        String username = (
                (EditText) findViewById(R.id.editTextUsername)
        ).getText().toString();
        String email = (
                (EditText) findViewById(R.id.editTextEmail)
        ).getText().toString();
        User user = new User(username, email);

        // specify the path to store the data
        DatabaseReference ref = db.getReference("users");
        ref.child(user.getId()).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
                Log.d("Firebase", "User added successfully");
            } else {
                Toast.makeText(this, "Failed to add user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Failed to add user", task.getException());
            }
        });

        // clear input fields
        ((EditText) findViewById(R.id.editTextUsername)).setText("");
        ((EditText) findViewById(R.id.editTextEmail)).setText("");

    }
}
