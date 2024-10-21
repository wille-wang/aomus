package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityAddBinding;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private List<TextView> textViewList = new ArrayList<>();
    private int day = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddBinding binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textViewList.add(binding.tvDay1);
        textViewList.add(binding.tvDay2);
        textViewList.add(binding.tvDay3);
        textViewList.add(binding.tvDay4);
        textViewList.add(binding.tvDay5);
        textViewList.add(binding.tvDay6);
        textViewList.add(binding.tvDay7);
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = binding.etClassName.getText().toString();
                String time1 = binding.tvTime1.getText().toString();
                String time2 = binding.tvTime2.getText().toString();
                String content = className + "\nCOMP90018\n" + time1 + "-" + time2 + "\n" + "PAR123";
                Intent data = new Intent();
                data.putExtra("day", day);
                data.putExtra("content", content);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        for (TextView textView : textViewList) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (TextView textView : textViewList) {
                        textView.setBackgroundResource(R.drawable.shape_background1);
                    }
                    textView.setBackgroundResource(R.drawable.shape_background2);
                    List<String> dataList = new ArrayList<>();
                    int id = v.getId();
                    if (id == R.id.tv_day1) {
                        day = 1;
                    } else if (id == R.id.tv_day2) {
                        day = 2;
                    } else if (id == R.id.tv_day3) {
                        day = 3;
                    } else if (id == R.id.tv_day4) {
                        day = 4;
                    } else if (id == R.id.tv_day5) {
                        day = 5;
                    } else if (id == R.id.tv_day6) {
                        day = 6;
                    } else if (id == R.id.tv_day7) {
                        day = 7;
                    }
                }
            });
        }
    }
}