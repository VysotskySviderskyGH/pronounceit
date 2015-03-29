package com.vsgh.pronounceit.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.adapters.RVAdapter;
import com.vsgh.pronounceit.customviews.QRBarDecoration;
import com.vsgh.pronounceit.persistence.Sounds;
import com.vsgh.pronounceit.customviews.TargetedSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Eren on 18.02.2015.
 */
public class CardActivity extends BaseVsghActivity {

    // An array of meaningless titles
    private static final String[] someTitles = {
            "Test 1",
            "Test 2",
            "Test 3",
    };
    public TargetedSwipeRefreshLayout swiper;
    Context context;
    private RecyclerView mRecycler;
    private StaggeredGridLayoutManager mSGLM;
    private LinearLayout qrBar;
    private TextView tvQRBarTitle;
    private RVAdapter mAdapter;
    private ArrayList<String> myDataset;
    private Animation inAnim;
    private Animation outAnim;
    private TextToSpeech mTTS;
    // For randomizing titles across our dataset
    private Random randy = new Random();

    // See QRBar setup below
    private int columnCount;
    private int qrBarHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity_layout);
        context = this;
    }

    @Override
    protected void configureViews() {

        //////////////////////
        //  Setup QR Bar    //
        //////////////////////
        inAnim = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);
        outAnim = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        qrBar = (LinearLayout) findViewById(R.id.myQRBar);
        // In production, better to get this from a "values.xml" resource
        // in a res folder appropriate to screen size / orientation
        columnCount = 2;
        // Set the QRBar Height to that of the ActionBar
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            qrBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        tvQRBarTitle = (TextView) findViewById(R.id.tvQRBarTitle);
        tvQRBarTitle.setText("Tap to add card");
        tvQRBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Add card");
                alertDialog.setMessage("Enter word");
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
                                if (input.getText().toString().length() > 2) {
                                    addItemAtPosition(0, input.getText().toString());
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
        });
        //////////////////////////////
        //  Setup Swipe To Refresh  //
        //////////////////////////////
        swiper = (TargetedSwipeRefreshLayout) findViewById(R.id.swipe_container);
        swiper.setSize(SwipeRefreshLayout.LARGE);
        swiper.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light
        );
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // This is where you should kickoff the
                // refreshing task.

                // For now, just wait a few seconds and turn off refreshing.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (myDataset != null && mAdapter != null) {
                            // Collections.shuffle(myDataset);
                            mAdapter.notifyDataSetChanged();
                        }
                        swiper.setRefreshing(false);
                    }
                }, 0);
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
        myDataset = new ArrayList<String>();
        // Load up the dataset with random titles
        List<Sounds> sounds = Sounds.listAll(Sounds.class);
        for (Sounds sound : sounds) {
            myDataset.add(sound.getName());
        }
        mAdapter = new RVAdapter(this, myDataset, mTTS);
        // Set the RecyclerView's Adapter
        mRecycler.setAdapter(mAdapter);

        // Set the Recyclerview to be the target scrollable view
        // for the TargetedSwipeRefreshAdapter.
        swiper.setTargetScrollableView(mRecycler);
    }

    /**
     * Adds an item at postion, and scrolls to that position.
     *
     * @param position index in dataset
     * @param item     to add
     */
    public void addItemAtPosition(int position, String item) {
        myDataset.add(position, item);
        mAdapter.notifyItemInserted(position);
        mSGLM.scrollToPosition(position);
        Sounds sounds = new Sounds(item);
        sounds.save();
    }
}
