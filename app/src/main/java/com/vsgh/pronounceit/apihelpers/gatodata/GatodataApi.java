package com.vsgh.pronounceit.apihelpers.gatodata;

import android.os.AsyncTask;

import com.vsgh.pronounceit.persistence.Sentence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

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
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String json = downloadJSON(new URL(JSON_URL));
                    getSentenceList(json);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
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

            private void getSentenceList(String jsonString) throws JSONException {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    Long id = Long.parseLong(object.optString(ID_PARAM));
                    String text = object.optString(TEXT_PARAM);
                    String link = object.optString(NAME_PARAM);
                    int lessonId = Integer.parseInt(object.optString(LESSON_ID_PARAM));
                    Sentence sentence = new  Sentence(id, text, link, lessonId, false);
                    sentence.save();
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

        }.execute();
    }


}
