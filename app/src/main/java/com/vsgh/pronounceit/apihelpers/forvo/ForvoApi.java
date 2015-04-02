package com.vsgh.pronounceit.apihelpers.forvo;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.vsgh.pronounceit.R;
import com.vsgh.pronounceit.adapters.RVAdapter;
import com.vsgh.pronounceit.persistence.Sounds;
import com.vsgh.pronounceit.utils.ConnChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Slawa on 2/8/2015.
 */
public class ForvoApi {

    public static void downloadMp3Url(final Context context, final String word,
                                      final RVAdapter rvAdapter,
                                      final int position, final ArrayList arrayList) {
        final String url = ForvoParams.getUrl(word);
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String mp3url = "";
                try {
                    if(ConnChecker.isOnline(context)){
                        String json = downloadJSON(new URL(params[0]));
                        mp3url = getDirectUrl(json);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(!ConnChecker.isOnline(context)){
                    mp3url = "noCon";
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
                if (!url.equals("") && !url.equals("noCon")) {
                    WordsLoader.downloadWord(context, url, word);
                } else if (!url.equals("noCon")) {
                    Crouton.makeText((android.app.Activity) context, context.getString(
                            R.string.incorrectly_words), Style.INFO).show();
                    List<Sounds> sounds = Sounds.find(Sounds.class, "name = ?", word);
                    sounds.get(0).delete();
                    arrayList.remove(position);
                    rvAdapter.notifyItemRangeRemoved(position, 1);
                }
            }

            @Override
            protected void onPreExecute() {
            }
        }.execute(url);
    }
}
