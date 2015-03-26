package com.vsgh.pronounceit.apihelpers.forvo;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

/**
 * Created by Slawa on 3/26/2015.
 */
public class WordsLoader {
    public static void downloadWord(Context context, String link, final String word) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(word);
        request.setDescription("Downloading: " + word);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalFilesDir(context, null, word + ".mp3");
        request.setVisibleInDownloadsUi(false);
        final long downloadID = downloadManager.enqueue(request);
        String downloadCompleteIntentName = DownloadManager.ACTION_DOWNLOAD_COMPLETE;
        IntentFilter downloadCompleteIntentFilter = new IntentFilter(downloadCompleteIntentName);
        BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
                if (id != downloadID) {
                    //TODO Handle in DB and interface
                } else {

                    //TODO Handle in DB and interface
                }
            }
        };
        context.registerReceiver(downloadCompleteReceiver, downloadCompleteIntentFilter);
    }
}
