package com.vsgh.pronounceit.apphelpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Slawa on 2/1/2015.
 */
public class SharedPrefsHelper {
    public static final String SHARED_PREFS_NAME = "com.vsgh.pronounceit.SHARED_PREFS";

    public static void writeIntToSP(Context paramContext, String key, int value) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit();
        localEditor.putInt(key, value);
        localEditor.apply();
    }

    public static void writeStringToSP(Context paramContext, String key, String value) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit();
        localEditor.putString(key, value);
        localEditor.apply();
    }
}
