package com.vsgh.pronounceit.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Slawa on 3/28/2015.
 */
public class ConnChecker {
    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }
}
