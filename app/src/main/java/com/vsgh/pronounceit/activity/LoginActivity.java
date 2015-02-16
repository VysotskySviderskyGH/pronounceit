package com.vsgh.pronounceit.activity;

import android.os.Bundle;
import android.view.View;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;

/**
 * Created by Slawa on 2/4/2015.
 */
public class LoginActivity extends BaseVsghActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
    }

    @Override
    protected void configureViews() {
        aq.id(R.id.imgbtn_close).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        aq.id(R.id.btn_login_facebook).clicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
