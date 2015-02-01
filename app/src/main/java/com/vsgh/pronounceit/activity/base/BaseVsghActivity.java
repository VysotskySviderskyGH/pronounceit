package com.vsgh.pronounceit.activity.base;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

import com.facebook.AppEventsLogger;

/**
 * Created by Slawa on 2/1/2015.
 */
public abstract class BaseVsghActivity extends ActionBarActivity {

    protected void startActivityWithoutParams(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }
}
