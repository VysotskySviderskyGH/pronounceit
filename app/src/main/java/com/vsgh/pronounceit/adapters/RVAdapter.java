package com.vsgh.pronounceit.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vsgh.pronounceit.Constants;
import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.customviews.SwipeDismissTouchListener;
import com.vsgh.pronounceit.persistence.Sounds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Eren on 18.02.2015.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>
        implements View.OnClickListener {

    //    private static Random randy = new Random();
    // Hold the position of the expanded item
    private ArrayList<String> mDataset;
    private Context mContext;
    private MediaPlayer mediaPlayer;

    public RVAdapter(Context context, ArrayList<String> myDataset, TextToSpeech mTTS) {
        this.mDataset = myDataset;
        this.mContext = context;
    }

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int i) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        final ViewHolder holder = new ViewHolder(v);
        // Sets the click adapter for the entire cell
        // to the one in this class.
        holder.itemView.setOnClickListener(RVAdapter.this);
        holder.itemView.setTag(holder);
        holder.itemView.setOnTouchListener(new SwipeDismissTouchListener(
                holder.itemView,
                null,
                new SwipeDismissTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view, Object token) {
                        parent.removeView(holder.itemView);
                        List<Sounds> sounds = Sounds.find(Sounds.class, "name = ?",
                                mDataset.get(holder.getLayoutPosition()));
                        sounds.get(0).delete();
                        mDataset.remove(sounds.get(0).getName());
                        notifyItemRangeRemoved(holder.getLayoutPosition(), 1);
                        if (!Environment.getExternalStorageState().equals(
                                Environment.MEDIA_MOUNTED)) {
                            return;
                        }
                        File file = Environment.getExternalStorageDirectory();
                        file = new File(file.getAbsolutePath() + "/" +
                                Constants.DESTINATION_URI +
                                sounds.get(0).getName() + ".mp3");
                        file.delete();
                    }
                }));
        return holder;
    }

    @Override
    public void onBindViewHolder(RVAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        String theString = mDataset.get(holder.getLayoutPosition());
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" +
                Constants.DESTINATION_URI +
                theString + ".mp3");
        List<Sounds> sounds = Sounds.find(Sounds.class, "name = ?",
                mDataset.get(holder.getLayoutPosition()));
        if (!sounds.get(0).getDownloaded()) {
            Crouton.makeText((android.app.Activity) mContext, "This card cannot be listened", Style.INFO).show();
            return;
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(String.valueOf(sdPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.start();
    }

    /**
     * Create a ViewHolder to represent your cell layout
     * and data element structure
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvSubTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSubTitle = (TextView) itemView.findViewById(R.id.tvSubTitle);
        }
    }
}
