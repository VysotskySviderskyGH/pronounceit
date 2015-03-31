package com.vsgh.pronounceit.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.adapters.RVAdapter;
import com.vsgh.pronounceit.apihelpers.forvo.ForvoApi;
import com.vsgh.pronounceit.customviews.QRBarDecoration;
import com.vsgh.pronounceit.persistence.Sounds;
import com.vsgh.pronounceit.singletones.FontContainer;

import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Eren on 18.02.2015.
 */
public class CardActivity extends BaseVsghActivity {

    Context context;
    private RecyclerView mRecycler;
    private StaggeredGridLayoutManager mSGLM;
    private LinearLayout qrBar;
    private TextView tvQRBarTitle;
    private RVAdapter mAdapter;
    private ArrayList<String> myDataset;
    private TextToSpeech mTTS;
    private int columnCount;
    private int qrBarHeight;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity_layout);
        context = this;
    }

    @Override
    protected void configureViews() {
        aq.id(R.id.tvQRBarTitle).typeface(FontContainer.lanenar);
        //////////////////////
        //  Setup QR Bar    //
        //////////////////////
        qrBar = (LinearLayout) findViewById(R.id.myQRBar);
        columnCount = 2;
        // Set the QRBar Height to that of the ActionBar
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            qrBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        tvQRBarTitle = (TextView) findViewById(R.id.tvQRBarTitle);
        tvQRBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kostul();
            }
        });
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kostul();
            }
        });
        //////////////////////////////////////////////
        //  Grab the StaggeredGrid & Layout Manager //
        //////////////////////////////////////////////
        mRecycler = (RecyclerView) findViewById(R.id.rvExampleGrid);
        mRecycler.addItemDecoration(new QRBarDecoration(columnCount, qrBarHeight));
        mSGLM = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(mSGLM);
        //////////////////////////////
        //  Setup Adapter & DataSet //
        //////////////////////////////
        myDataset = new ArrayList<>();
        List<Sounds> sounds = Sounds.listAll(Sounds.class);
        for (Sounds sound : sounds) {
            myDataset.add(sound.getName());
        }
        mAdapter = new RVAdapter(this, myDataset, mTTS);
        mRecycler.setAdapter(mAdapter);
    }

    public void addItemAtPosition(int position, String item) {
        myDataset.add(position, item);
        mAdapter.notifyItemInserted(position);
        Sounds sounds = new Sounds(item, false);
        sounds.save();
        ForvoApi.downloadMp3Url(this, sounds.getName());
    }

    private void kostul() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Add a card");
        alertDialog.setMessage("Enter a word");
        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().length() > 1) {
                            List<Sounds> sounds = Sounds.listAll(Sounds.class);
                            for (Sounds temp : sounds)
                                if (temp.getName().toLowerCase().equals(
                                        input.getText().toString().toLowerCase())) {
                                    Crouton.makeText((android.app.Activity) context,
                                            getString(R.string.already_exists), Style.INFO).show();
                                    return;
                                }
                            String str = input.getText().toString();
                            String goodStr = str.trim();
                            int k = goodStr.indexOf(" ");
                            if (k == -1) {
                                addItemAtPosition(myDataset.size(), goodStr);
                            } else {
                                addItemAtPosition(myDataset.size(), str.substring(0, k));
                            }
                        }
                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}
