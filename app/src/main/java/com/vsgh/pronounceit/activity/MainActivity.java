package com.vsgh.pronounceit.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.cocosw.bottomsheet.BottomSheet;
import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.singletones.FontContainer;

/**
 * Created by Slawa on 2/1/2015.
 */
public class MainActivity extends BaseVsghActivity {

    private final AQuery aQuery = new AQuery(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        configureViews();
    }

    private void configureViews() {
        aQuery.id(R.id.txt_title).typeface(FontContainer.billabong);
        aQuery.id(R.id.txt_slogan).typeface(FontContainer.lanenar);
    }
}
