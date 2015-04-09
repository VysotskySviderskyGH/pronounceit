package com.vsgh.pronounceit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;


import com.crashlytics.android.Crashlytics;
import com.orm.SugarApp;
import com.vsgh.pronounceit.activity.HelpActivity;
import com.vsgh.pronounceit.apihelpers.gatodata.GatodataApi;
import com.vsgh.pronounceit.persistence.User;
import com.vsgh.pronounceit.singletones.FontContainer;
import com.vsgh.pronounceit.utils.ConnChecker;
import com.vsgh.pronounceit.utils.SharedPrefsHelper;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Slawa on 2/1/2015.
 */
public class PronounceItApp extends SugarApp {
    private static final String BILLABONG_FONT = "billabong_regular.ttf";
    private static final String LANE_NARROW_FONT = "lanenar.ttf";
    private static final String MYRIAD_PRO_FONT = "myriadpro_regular.otf";
    private SharedPreferences settings;

    public static final String MUST_BLOCK_BTN = "must_block_btn";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initFonts();
        settings = getSharedPreferences(Constants.PREFS_NAME,
                Context.MODE_PRIVATE);
        boolean firstStart = settings.getBoolean(
                Constants.FIRST_START, true);
        if (firstStart) {
            User user = new User("John Smith",0,0);
            user.save();
            SharedPrefsHelper.writeStringToSP(this,
                    Constants.CURRENT_USER, user.getUsername());
            downloadSentences();
            SharedPrefsHelper.writeBooleanToSP(settings,
                    Constants.FIRST_START, false);
            Intent i = new Intent(this, HelpActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(MUST_BLOCK_BTN,true);
            startActivity(i);
        }
    }

    private void downloadSentences() {
        if (ConnChecker.isOnline(this)) {
            GatodataApi.downloadSentenceList(this, false);
        }
    }

    private void initFonts() {
        FontContainer.billabong = Typeface.createFromAsset(getAssets(), BILLABONG_FONT);
        FontContainer.lanenar = Typeface.createFromAsset(getAssets(), LANE_NARROW_FONT);
        FontContainer.myriadpro = Typeface.createFromAsset(getAssets(), MYRIAD_PRO_FONT);
    }

}


