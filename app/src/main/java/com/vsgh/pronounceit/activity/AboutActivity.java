package com.vsgh.pronounceit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.singletones.FontContainer;

public class AboutActivity extends BaseVsghActivity {
    private int touchCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_DOWN) {
            if (touchCount++ == 3) {
                aq.id(R.id.header).visibility(View.GONE);
                aq.id(R.id.cat).visibility(View.VISIBLE);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void configureViews() {
        aq.id(R.id.tv_app_created).typeface(FontContainer.lanenar);
        aq.id(R.id.tv_gh).typeface(FontContainer.myriadpro)
                .textColor(getResources().getColor(R.color.s_orange))
                .clicked(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.geekhub.ck.ua"));
                        startActivity(i);
                    }
                });
        aq.id(R.id.tv_cr).typeface(FontContainer.myriadpro)
                .textColor(Color.LTGRAY);
        aq.id(R.id.btn_ok).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
