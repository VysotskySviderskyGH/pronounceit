package com.vsgh.pronounceit.logic;

import android.content.Context;
import android.net.Uri;

import com.androidquery.AQuery;
import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.apihelpers.gatodata.GatodataApi;
import com.vsgh.pronounceit.persistence.Sentence;
import com.vsgh.pronounceit.singletones.SentencesContainer;

import java.util.Random;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Slawa on 3/27/2015.
 */
public class GameHandler {

    private final Random random = new Random();
    private AQuery aQuery;
    private Context context;
    private Sentence currentSentence;

    public GameHandler(AQuery aQuery, Context context) {
        this.aQuery = aQuery;
        this.context = context;
        init();
        if (currentSentence == null) {
            setAnotherSentence();
            updateView();
        }
    }

    private void init() {
        if (SentencesContainer.sentenceList == null) {
            SentencesContainer.sentenceList = Sentence.listAll(Sentence.class);
        }
    }

    private void setAnotherSentence() {
        //TODO Handle situation with repeating words and with interaction with DB
        currentSentence = SentencesContainer
                .sentenceList.get(random.nextInt(SentencesContainer.sentenceList.size() - 1));
    }

    private boolean isCorrect(String result) {
        //TODO Will ignore exclamation marks etc when method will compare strings
        return result.equalsIgnoreCase(currentSentence.getSentence());
    }

    private void handleSuccess() {
        //TODO Handle interaction with DB
    }

    private void handleUnsuccess() {
        //TODO Handle interaction with DB
    }

    public void sendResultToHandler(String result) {
        if (isCorrect(result)) {
            handleSuccess();
            Crouton.makeText((BaseVsghActivity) context,
                    context.getString(R.string.said_msg) + " " + result,
                    Style.CONFIRM).show();
        } else {
            handleUnsuccess();
            Crouton.makeText((BaseVsghActivity) context,
                    context.getString(R.string.said_msg) + " " + result,
                    Style.ALERT).show();
        }
        setAnotherSentence();
        updateView();
    }

    public void skip() {
        setAnotherSentence();
        updateView();
    }

    private void updateView() {
        aQuery.id(R.id.question_text).text(currentSentence.getSentence());
    }

    public Sentence getCurrentSentence() {
        return currentSentence;
    }

    public Uri getUri() {
        String url = GatodataApi.SOUNDS_BASE_URL + currentSentence.getLessonId() + "/" +
                currentSentence.getLink();
        return Uri.parse(url);
    }
}
