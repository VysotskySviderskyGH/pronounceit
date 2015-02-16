package com.vsgh.pronounceit.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.androidquery.AQuery;
import com.facebook.AppEventsLogger;
import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.singletones.FontContainer;

/**
 * Created by Slawa on 2/1/2015.
 */
public abstract class BaseVsghActivity extends ActionBarActivity {

    protected final AQuery aq = new AQuery(this);

    protected void startActivityWithoutParams(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

    protected abstract void configureViews();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        aq.id(R.id.txt_title).typeface(FontContainer.billabong);
        aq.id(R.id.txt_slogan).typeface(FontContainer.lanenar);
        configureViews();
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
