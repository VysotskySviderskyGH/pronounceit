package com.vsgh.pronounceit.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.apihelpers.gatodata.GatodataApi;
import com.vsgh.pronounceit.persistence.Sentence;
import com.vsgh.pronounceit.singletones.FontContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Slawa on 2/13/2015.
 */
public class GameActivity extends BaseVsghActivity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private MediaPlayer mediaPlayer;
    private int currentSentenceId;
    private final Random random = new Random();
    Sentence sentence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_layout);
        mediaPlayer = new MediaPlayer();
        currentSentenceId = 2;
    }

    @Override
    protected void configureViews() {
       // List<Sentence> sentences = Sentence.find(Sentence.class, "listen = ?", "false");
        List<Sentence> sentences = Sentence.listAll(Sentence.class);
        sentence = sentences.get(random.nextInt(sentences.size()-1));

        String question = sentence.getSentence();

            aq.id(R.id.question_text).text(question);

        aq.id(R.id.question_text).typeface(FontContainer.lanenar);
        aq.id(R.id.btn_get_voice).longClicked(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                promptSpeechInput();
                return true;
            }
        });
        aq.id(R.id.btn_listen).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    return;
                }
                String url = GatodataApi.SOUNDS_BASE_URL + sentence.getLessonId() + "/" +
                        sentence.getLink();
                Log.d("URL", url);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.reset();
                    }
                });
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.say_sentence));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.gaps_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(this, result.get(0), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
