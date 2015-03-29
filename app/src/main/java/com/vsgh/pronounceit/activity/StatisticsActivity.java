package com.vsgh.pronounceit.activity;

import android.os.Bundle;
import android.view.View;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;

/**
 * Created by Slawa on 2/4/2015.
 */
public class StatisticsActivity extends BaseVsghActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity_layout);
    }

    @Override
    protected void configureViews() {
        aq.id(R.id.connect).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithoutParams(LoginActivity.class);
            }
        });
    }
}
