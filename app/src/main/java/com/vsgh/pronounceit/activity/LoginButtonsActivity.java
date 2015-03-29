package com.vsgh.pronounceit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.googleplus.GooglePlusSocialNetwork;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;

public class LoginButtonsActivity extends BaseVsghActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_buttons);
    }

    @Override
    protected void configureViews() {
        aq.id(R.id.btn_login_vk).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent().putExtra(StatisticsActivity.LOGIN_RESULT_KEY,
                        VkSocialNetwork.ID));
                finish();
            }
        });
        aq.id(R.id.btn_login_googleplus).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent().putExtra(StatisticsActivity.LOGIN_RESULT_KEY,
                        GooglePlusSocialNetwork.ID));
                finish();
            }
        });
        aq.id(R.id.btn_login_facebook).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent().putExtra(StatisticsActivity.LOGIN_RESULT_KEY,
                        FacebookSocialNetwork.ID));
                finish();
            }
        });
        aq.id(R.id.imgbtn_close).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
