package com.vsgh.pronounceit.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.logic.GameHandler;
import com.vsgh.pronounceit.singletones.FontContainer;
import com.vsgh.pronounceit.utils.ConnChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Slawa on 2/13/2015.
 */
public class GameActivity extends BaseVsghActivity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private MediaPlayer mediaPlayer;
    private GameHandler gameHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_layout);
        mediaPlayer = new MediaPlayer();
        gameHandler = new GameHandler(aq, this);
    }

    @Override
    protected void configureViews() {
        aq.id(R.id.question_text).typeface(FontContainer.lanenar);
        aq.id(R.id.btn_get_voice).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput(gameHandler.getCurrentSentence().getSentence());
            }
        });
        aq.id(R.id.btn_skip).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameHandler.skip();
            }
        });
        aq.id(R.id.btn_listen).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnChecker.isOnline(GameActivity.this)) {
                    if (mediaPlayer.isPlaying()) {
                        return;
                    }
                    aq.id(R.id.btn_listen).enabled(false);
                    final Uri uri = gameHandler.getUri();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(GameActivity.this, uri);
                    } catch (IOException | IllegalStateException e) {
                        e.printStackTrace();
                        mediaPlayer.stop();
                        return;
                    }
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            if (mp == mediaPlayer) {
                                mp.start();
                            }
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            try {
                                if (mp == mediaPlayer) {
                                    mp.reset();
                                }
                            } catch (IllegalArgumentException | IllegalStateException e) {
                                e.printStackTrace();
                            } finally {
                                aq.id(R.id.btn_listen).enabled(true);
                            }
                        }
                    });
                } else {
                    Crouton.makeText(GameActivity.this, getString(R.string.interner_error), Style.INFO).show();
                }
            }
        });
    }

    private void promptSpeechInput(String text) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Crouton.makeText(GameActivity.this,
                    getString(R.string.gaps_error),
                    Style.ALERT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    gameHandler.sendResultToHandler(result.get(0));
                } else {
                    Crouton.makeText(this, getString(R.string.error_with_input), Style.INFO).show();
                    gameHandler.skip();
                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }
}
