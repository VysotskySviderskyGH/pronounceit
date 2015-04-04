package com.vsgh.pronounceit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

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

    public static void writeBooleanToSP(Context paramContext,
                                        final String name, final boolean value) {
        SharedPreferences.Editor editor = paramContext
                .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static
    @NonNull
    String readStringFromSP(Context paramContext,
                            final String key, final String defValue) {
        SharedPreferences sharedPreferences = paramContext
                .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defValue);
    }

    public static
    @NonNull
    Integer readIntFromSP(Context paramContext,
                          final String key, final int defValue) {
        SharedPreferences sharedPreferences = paramContext
                .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defValue);
    }

    public static
    @NonNull
    Boolean readBooleanFromSP(Context paramContext,
                              final String key, final boolean defValue) {
        SharedPreferences sharedPreferences = paramContext
                .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defValue);
    }
}
