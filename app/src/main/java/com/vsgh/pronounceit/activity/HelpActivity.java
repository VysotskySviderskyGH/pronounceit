package com.vsgh.pronounceit.activity;

import android.os.Bundle;
import android.view.View;

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
        aq.id(R.id.description).typeface(FontContainer.lanenar);
        aq.id(R.id.btn_ok).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
