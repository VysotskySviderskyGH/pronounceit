package com.vsgh.pronounceit.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.apihelpers.gatodata.GatodataApi;
import com.vsgh.pronounceit.persistence.Sentence;
import com.vsgh.pronounceit.utils.Cleaner;
import com.vsgh.pronounceit.utils.ConnChecker;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Slawa on 2/1/2015.
 */
public class MainActivity extends BaseVsghActivity {

    private ProgressDialog mProgressDialog;

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
                if (Sentence.listAll(Sentence.class).isEmpty()) {
                    if (ConnChecker.isOnline(MainActivity.this)) {
                        showProgress(getString(R.string.download_words));
                        GatodataApi.downloadSentenceList(MainActivity.this, true);
                    } else {
                        Crouton.makeText(MainActivity.this, getString(R.string.interner_error), Style.INFO).show();
                    }
                } else {
                    startActivityWithoutParams(GameActivity.class);
                }
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

    protected void showProgress(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }
}
