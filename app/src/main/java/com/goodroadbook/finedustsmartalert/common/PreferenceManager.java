package com.goodroadbook.finedustsmartalert.common;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager
{
    public PreferenceManager()
    {
        ;
    }

    public static void setIntPreference(Context context, String key, int value)
    {
        SharedPreferences preference = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editPreference = preference.edit();
        editPreference.putInt(key, value);
        editPreference.commit();
    }

    public static int getIntPreference(Context context, String key, int defaultValue)
    {
        SharedPreferences preference = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return preference.getInt(key, defaultValue);
    }

    public static void setStringPreference(Context context, String key, String value)
    {
        SharedPreferences preference = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editPreference = preference.edit();
        editPreference.putString(key, value);
        editPreference.commit();
    }

    public static String getStringPreference(Context context, String key, String defaultValue)
    {
        SharedPreferences preference = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return preference.getString(key, defaultValue);
    }
}
