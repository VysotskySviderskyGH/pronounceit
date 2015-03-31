package com.vsgh.pronounceit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.orm.SugarApp;
import com.vsgh.pronounceit.activity.HelpActivity;
import com.vsgh.pronounceit.apihelpers.gatodata.GatodataApi;
import com.vsgh.pronounceit.apphelpers.SharedPrefsHelper;
import com.vsgh.pronounceit.singletones.FontContainer;
import com.vsgh.pronounceit.utils.ConnChecker;

/**
 * Created by Slawa on 2/1/2015.
 */
public class PronounceItApp extends SugarApp {
    private static final String BILLABONG_FONT = "billabong_regular.ttf";
    private static final String LANE_NARROW_FONT = "lanenar.ttf";
    private static final String MYRIAD_PRO_FONT = "myriadpro_regular.otf";
    private SharedPreferences settings;

    @Override
    public void onCreate() {
        super.onCreate();
        initFonts();
        settings = getSharedPreferences(Constants.PREFS_NAME,
                Context.MODE_PRIVATE);
        boolean firstStart = settings.getBoolean(
                Constants.FIRST_START, true);
        if (firstStart) {
            downloadSentences();
            SharedPrefsHelper.writeBooleanToSP(settings,
                    Constants.FIRST_START, false);
            Intent i = new Intent(this, HelpActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    private void downloadSentences() {
        if (ConnChecker.isOnline(this)) {
            GatodataApi.downloadSentenceList();
        } else {
            //TODO Handle situation when user won't have internet
        }

    }

    private void initFonts() {
        FontContainer.billabong = Typeface.createFromAsset(getAssets(), BILLABONG_FONT);
        FontContainer.lanenar = Typeface.createFromAsset(getAssets(), LANE_NARROW_FONT);
        FontContainer.myriadpro = Typeface.createFromAsset(getAssets(), MYRIAD_PRO_FONT);
    }

}


