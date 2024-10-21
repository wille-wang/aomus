package com.example.myapplication.ui.schedule;

import android.util.SparseArray;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ScheduleViewModel extends ViewModel {
    private SparseArray<List<String>> dataArray = new SparseArray<>();

    public List<String> getDataArray(int day) {
        List<String> strings = dataArray.get(day);
        return strings == null ? new ArrayList<>() : strings;
    }

    public void setData(int day, String content) {
        List<String> strings = dataArray.get(day);
        if (strings == null) {
            strings = new ArrayList<>();
        }
        strings.add(content);
        dataArray.put(day, strings);
    }

}
