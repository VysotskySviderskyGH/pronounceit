package com.vsgh.pronounceit.activity;

import android.os.Bundle;
import android.view.View;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;

/**
 * Created by Slawa on 2/1/2015.
 */
public class MainActivity extends BaseVsghActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
    }

    @Override
    protected void configureViews() {
        aq.id(R.id.btn_game).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithoutParams(GameActivity.class);
            }
        });
        aq.id(R.id.btn_rating).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithoutParams(StatisticsActivity.class);
            }
        });
        aq.id(R.id.btn_cards).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithoutParams(CardActivity.class);
            }
        });
        aq.id(R.id.btn_help).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithoutParams(HelpActivity.class);
            }
        });
    }
}
