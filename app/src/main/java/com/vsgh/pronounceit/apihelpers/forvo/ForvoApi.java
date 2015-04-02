package com.vsgh.pronounceit.apihelpers.forvo;

import android.content.Context;
import android.os.AsyncTask;

import com.vsgh.pronounceit.persistence.Sounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Slawa on 2/8/2015.
 */
public class ForvoApi {
    public static void downloadMp3Url(final Context context, final String word) {
        final String url = ForvoParams.getUrl(word);
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String mp3url = "";
                try {
                    String json = downloadJSON(new URL(params[0]));
                    mp3url = getDirectUrl(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mp3url;
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

            private String getDirectUrl(String jsonString) throws JSONException {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray array = jsonObject.optJSONArray("items");
                JSONObject mainObject = array.optJSONObject(0);
                return mainObject.optString("pathmp3");
            }

            @Override
            protected void onPostExecute(String url) {
                if (url != null) {
                    WordsLoader.downloadWord(context, url, word);
                } else {
                    //TODO Handle situation when word is incorrect
                }
            }

            @Override
            protected void onPreExecute() {
            }
        }.execute(url);
    }
}
