package com.example.rrosatti.memorykeeper.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rrosatti on 10/21/17.
 */

public class Util {

    public static String toStringDate(int day, int month, int year) {
        String sDay = String.valueOf(day);
        String sMonth = String.valueOf(month);
        if (day < 10)
            sDay = "0" + day;
        if (month < 10)
            sMonth = "0" + month;

        return sDay+"/"+sMonth+"/"+year;
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(c.getTime());
    }

    public static void disableUserInteraction(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void enableUserInteraction(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
