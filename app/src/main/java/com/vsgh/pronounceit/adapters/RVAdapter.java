package com.vsgh.pronounceit.adapters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.persistence.Sounds;
import com.vsgh.pronounceit.utils.SwipeDismissTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Eren on 18.02.2015.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>
        implements View.OnClickListener, TextToSpeech.OnInitListener {

    //    private static Random randy = new Random();
    // Hold the position of the expanded item
    private ArrayList<String> mDataset;
    private Context mContext;
    private TextToSpeech mTTS;

    public RVAdapter(Context context, ArrayList<String> myDataset, TextToSpeech mTTS) {
        this.mDataset = myDataset;
        this.mContext = context;
        this.mTTS = new TextToSpeech(mContext, this);
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
                        List<Sounds> sounds = Sounds.listAll(Sounds.class);
                        for (Sounds temp : sounds) {
                            if (temp.getName().equals(mDataset.get(holder.getLayoutPosition()))) {
                                Sounds sounds1 = Sounds.findById(Sounds.class, temp.getId());
                                sounds1.delete();
                                mDataset.remove(temp.getName());
                                notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                }));
        return holder;
    }

    @Override
    public void onBindViewHolder(RVAdapter.ViewHolder holder, int position) {
//        int colorIndex = randy.nextInt(bgColors.length);
        holder.tvTitle.setText(mDataset.get(position));
//        holder.tvTitle.setBackgroundColor(bgColors[colorIndex]);
//        holder.tvSubTitle.setBackgroundColor(sbgColors[colorIndex]);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        String theString = mDataset.get(holder.getLayoutPosition());
        mTTS.speak(theString, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTTS.setLanguage(Locale.ENGLISH);
            //int result = mTTS.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Извините, этот язык не поддерживается");
            }
        } else {
            Log.e("TTS", "Ошибка!");
        }
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
