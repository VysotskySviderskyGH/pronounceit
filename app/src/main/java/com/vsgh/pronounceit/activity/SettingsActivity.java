package com.vsgh.pronounceit.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.utils.Cleaner;

/**
 * Created by Slawa on 4/5/2015.
 */
public class SettingsActivity extends BaseVsghActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void configureViews() {
        aq.id(R.id.deletecards_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(SettingsActivity.this);
                textView.setText(getString(R.string.delete_cards_msg));
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(0, 25, 0, 5);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(18);
                AlertDialog.Builder ad = new AlertDialog.Builder(SettingsActivity.this)
                        .setCancelable(true)
                        .setTitle(getString(R.string.really))
                        .setView(textView)
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Cleaner.cleanAllCards(SettingsActivity.this);
                            }
                        });
                ad.show();
            }
        });
        aq.id(R.id.clearinfo_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(SettingsActivity.this);
                textView.setText(getString(R.string.delinfo_msg));
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(0, 25, 0, 5);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(18);
                AlertDialog.Builder ad = new AlertDialog.Builder(SettingsActivity.this)
                        .setCancelable(true)
                        .setTitle(getString(R.string.really))
                        .setView(textView)
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Cleaner.cleanAllUserInfos();
                            }
                        });
                ad.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
