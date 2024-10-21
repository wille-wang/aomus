package com.example.myapplication.util;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DateUtils {

    public static List<Pair<String, String>> getDaysOfWeekInCurrentMonth() {

        List<Pair<String, String>> daysOfWeekList = new ArrayList<>();


        for (int i = 0; i < 7; i++) {

            daysOfWeekList.add(Pair.create((i+1) + "", getDayOfWeekString(i+1)));

        }

        // 返回结果列表
        return daysOfWeekList;
    }

    private static String getDayOfWeekString(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "M";
            case 2:
                return "T";
            case 3:
                return "W";
            case 4:
                return "T";
            case 5:
                return "F";
            case 6:
                return "S";
            case 7:
                return "S";
            default:
                return "";
        }
    }
}
