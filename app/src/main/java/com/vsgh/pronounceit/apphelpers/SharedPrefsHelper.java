package com.vsgh.pronounceit.apphelpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.vsgh.pronounceit.Constants;

/**
 * Created by Slawa on 2/1/2015.
 */
public class SharedPrefsHelper {

    public static void writeIntToSP(Context paramContext, String key, int value) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE).edit();
        localEditor.putInt(key, value);
        localEditor.apply();
    }

    public static void writeStringToSP(Context paramContext, String key, String value) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE).edit();
        localEditor.putString(key, value);
        localEditor.apply();
    }

    public static void writeBooleanToSP(SharedPreferences settings,
                                       final String name, final boolean value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }
}
