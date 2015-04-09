package com.vsgh.pronounceit.logic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.vsgh.pronounceit.Constants;
import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.activity.base.BaseVsghActivity;
import com.vsgh.pronounceit.apihelpers.gatodata.GatodataApi;
import com.vsgh.pronounceit.persistence.Sentence;
import com.vsgh.pronounceit.persistence.Sounds;
import com.vsgh.pronounceit.persistence.User;
import com.vsgh.pronounceit.persistence.UserSentence;
import com.vsgh.pronounceit.utils.SharedPrefsHelper;

import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;
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
    private String currentUser;
    private static List<Sentence> sentences;

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
        if (sentences == null) {
            currentUser = SharedPrefsHelper.readStringFromSP(context,
                    Constants.CURRENT_USER, "John Smith");
            List<UserSentence> userSentences = UserSentence.find(UserSentence.class,
                    "username = ?", currentUser);
             sentences = Sentence.listAll(Sentence.class);
            for(int i=0; i< sentences.size(); i++){
                for (int j=0; j < userSentences.size(); j++){
                    if(sentences.get(i).getSentence()
                            .equals(userSentences.get(j))){
                        sentences.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private void setAnotherSentence() {
        //TODO Handle situation with repeating words and with interaction with DB
        currentUser = SharedPrefsHelper.readStringFromSP(context,
                Constants.CURRENT_USER, "John Smith");
        List<UserSentence> userSentences = UserSentence.find(UserSentence.class,
                "username = ?", currentUser);
        sentences = Sentence.listAll(Sentence.class);
        for(int i=0; i< sentences.size(); i++){
            for (int j=0; j < userSentences.size(); j++){
                if(sentences.get(i).getSentence()
                        .equals(userSentences.get(j))){
                    sentences.remove(i);
                    break;
                }
            }
        }
        currentSentence = sentences
                .get(random.nextInt(sentences.size() - 1));
    }

    private String cleanString(String string) {
        return string.replace("!", "")
                .replace("?", "")
                .replace(",", "")
                .replace(".", "")
                .replace("'", "")
                .replace(" ", "");
    }

    private boolean isCorrect(String result) {
        return cleanString(result)
                .equalsIgnoreCase(cleanString(currentSentence.getSentence()));
    }

    private void handleSuccess() {
        currentUser = SharedPrefsHelper.readStringFromSP(context,
                Constants.CURRENT_USER, "John Smith");
        List<User> users = User.find(User.class, "username = ?", currentUser);
        int success = users.get(0).getSuccess() + 1;
        users.get(0).setSuccess(success);
        users.get(0).save();
        UserSentence userSentence = new UserSentence(currentUser, currentSentence.getSentence());
        userSentence.save();
    }

    private void handleUnsuccess() {
        currentUser = SharedPrefsHelper.readStringFromSP(context,
                Constants.CURRENT_USER, "John Smith");
        List<User> users = User.find(User.class, "username = ?", currentUser);
        int unsuccessful = users.get(0).getUnsuccessful() + 1;
        users.get(0).setUnsuccessful(unsuccessful);
        users.get(0).save();
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
        initClickableTextView(currentSentence.getSentence());
    }

    private void initClickableTextView(final String sentence) {
        aQuery.id(R.id.question_text).text(currentSentence.getSentence());
        aQuery.id(R.id.question_text).getTextView()
                .setMovementMethod(LinkMovementMethod.getInstance());
        Spannable spans = (Spannable) aQuery.id(R.id.question_text).getTextView().getText();
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(sentence);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String possibleWord = sentence.substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                spans.setSpan(clickSpan, start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }
    }

    private ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {
            private TextPaint ds;

            @Override
            public void onClick(View widget) {
                aQuery.show(createAddingDialog(word));
                changeSpanBgColor(widget);
            }

            public void changeSpanBgColor(View widget) {
                updateDrawState(ds);
                widget.invalidate();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                if (this.ds == null) {
                    this.ds = ds;
                }
                ds.setARGB(150, 0, 0, 0);
            }
        };
    }

    private Dialog createAddingDialog(final String word) {
        List<Sounds> soundses = Sounds.listAll(Sounds.class);
        for (Sounds temp : soundses) {
            if (temp.getName().toLowerCase().equals(word.toLowerCase())) {
                Toast.makeText(context, context.getString(R.string.already_exists), Toast.LENGTH_LONG).show();
                return null;
            }
        }
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 25, 0, 5);
        textView.setText(word.toUpperCase());
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(22);
        ad.setCancelable(true)
                .setTitle(context.getString(R.string.addcard_gialog))
                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(context.getString(R.string.add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Sounds sounds = new Sounds(word, false);
                        sounds.save();
                    }
                })
                .setView(textView);
        return ad.show();
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
