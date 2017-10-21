package com.example.rrosatti.memorykeeper.utils;

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
}
