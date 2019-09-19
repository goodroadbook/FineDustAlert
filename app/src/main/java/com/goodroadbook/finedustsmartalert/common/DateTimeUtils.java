package com.goodroadbook.finedustsmartalert.common;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils
{
    public static long changeDateTime(String dateTime)
    {
        @SuppressLint("SimpleDateFormat")
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = null;
        try
        {
            date = formatter.parse(dateTime);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        long longvalue = date.getTime();
        Log.d("namjinha", "long value = " + longvalue);

        return longvalue;
    }

    public static String changeLongTime(long longTime)
    {
        @SuppressLint("SimpleDateFormat")
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return formatter.format(longTime);
    }

    public static long getKoreaTime()
    {
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        return cal.getTimeInMillis();
    }
}
