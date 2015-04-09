package com.vsgh.pronounceit.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.vsgh.pronounceit.PronounceItApp;
import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.singletones.FontContainer;

public class HelpActivity extends BaseVsghActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_layout);
    }

    @Override
    protected void configureViews() {
        boolean mustBlockBtn = getIntent().getBooleanExtra(PronounceItApp.MUST_BLOCK_BTN,false);
        if(mustBlockBtn) {
            aq.id(R.id.btn_ok).enabled(false);
            new CountDownTimer(6*1000,1000){

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    aq.id(R.id.btn_ok).enabled(true);
                }
            }.start();
        }
        aq.id(R.id.description).typeface(FontContainer.lanenar);
        aq.id(R.id.btn_ok).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
