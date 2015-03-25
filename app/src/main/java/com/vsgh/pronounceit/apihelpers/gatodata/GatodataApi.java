package com.vsgh.pronounceit.apihelpers.gatodata;

import android.os.AsyncTask;
import android.util.Log;

import com.vsgh.pronounceit.entity.Sentence;
import com.vsgh.pronounceit.singletones.SentenceContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Slawa on 3/12/2015.
 */
public class GatodataApi {
    public static final String JSON_URL = "http://gatodata.com/pro/services/practices.php";
    public static final String SOUNDS_BASE_URL = "http://www.gatodata.com/pro/meodia/audio/";
    public static final String ID_PARAM = "id";
    public static final String TEXT_PARAM = "text";
    public static final String NAME_PARAM = "audio_link";
    public static final String LESSON_ID_PARAM = "lesson_id";

    public static void downloadSentenceList() {
        new AsyncTask<String, Void, List<Sentence>>() {
            @Override
            protected List<Sentence> doInBackground(String... params) {
                List<Sentence> sentenceList = Collections.emptyList();
                try {
                    String json = downloadJSON(new URL(JSON_URL));
                    sentenceList = getSentenceList(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return sentenceList;
            }

            private String downloadJSON(URL url) {
                try {
                    InputStream inputStream = url.openStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String tmpString;
                    while ((tmpString = bufferedReader.readLine()) != null) {
                        stringBuilder.append(tmpString);
                    }
                    return stringBuilder.toString();
                } catch (IOException e) {
                    return null;
                }
            }

            private List<Sentence> getSentenceList(String jsonString) throws JSONException {
                JSONArray jsonArray = new JSONArray(jsonString);
                List<Sentence> sentenceList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    int id = Integer.parseInt(object.optString(ID_PARAM));
                    String text = object.optString(TEXT_PARAM);
                    String link = object.optString(NAME_PARAM);
                    int lessonId = Integer.parseInt(object.optString(LESSON_ID_PARAM));
                    Sentence sentence = new Sentence(id, text, link, lessonId);
                    sentenceList.add(sentence);
                }
                return sentenceList;
            }

            @Override
            protected void onPostExecute(List<Sentence> sentenceList) {
                SentenceContainer.sentences = sentenceList;
                SentenceContainer.isInit = true;
                Log.d("URL", sentenceList.size() + "");
            }

            @Override
            protected void onPreExecute() {
            }
        }.execute();
    }


}
