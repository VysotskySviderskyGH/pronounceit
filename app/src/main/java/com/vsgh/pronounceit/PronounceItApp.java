package com.vsgh.pronounceit;

import android.graphics.Typeface;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orm.SugarApp;
import com.vsgh.pronounceit.singletones.FontContainer;

/**
 * Created by Slawa on 2/1/2015.
 */
public class PronounceItApp extends SugarApp {
    private static final String BILLABONG_FONT = "billabong_regular.ttf";
    private static final String LANE_NARROW_FONT = "lanenar.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        initFonts();
        initImageDownloader();
    }

    private void initFonts() {
        FontContainer.billabong = Typeface.createFromAsset(getAssets(), BILLABONG_FONT);
        FontContainer.lanenar = Typeface.createFromAsset(getAssets(), LANE_NARROW_FONT);
    }

    private void initImageDownloader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);
    }
}


