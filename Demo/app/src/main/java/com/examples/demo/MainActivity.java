package com.examples.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";

    private static final int DEFAULT_DIALER_REQUEST_ID = 83;
    public static final String TAG = "MADARA";
    String  name="";
    String  code="";

    private ImageButton bt_see_title_img;
    private ImageButton bt_chosed_title_img;
    private ImageButton add_information_img;
    private ImageButton bt_back_img;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ImageView icSearch = findViewById(R.id.ic_search);
        ImageView icCamera = findViewById(R.id.ic_camera);

        icSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TwoActivity.class);
                startActivity(intent);
            }
        });

        icCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThreeActivity.class);
                startActivity(intent);
            }
        });



        bt_see_title_img = (ImageButton) findViewById(R.id.see_title_img);
        bt_chosed_title_img = (ImageButton) findViewById(R.id.chosed_title_img);
        add_information_img = (ImageButton) findViewById(R.id.information_img);
        bt_back_img = (ImageButton) findViewById(R.id.back_img);

        bt_see_title_img.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent1);

            }
        });

        bt_chosed_title_img.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, FourActivity.class);
                startActivity(intent1);

            }
        });


        add_information_img.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent2);

            }
        });

        bt_back_img.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent2);
            }
        });

    }

}

