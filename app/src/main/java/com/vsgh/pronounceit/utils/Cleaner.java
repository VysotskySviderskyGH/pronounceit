package com.vsgh.pronounceit.utils;

import android.content.Context;

import com.vsgh.pronounceit.Constants;
import com.vsgh.pronounceit.persistence.Sentence;
import com.vsgh.pronounceit.persistence.Sounds;
import com.vsgh.pronounceit.persistence.User;

import java.io.File;
import java.io.IOException;

/**
 * Created by Slawa on 4/4/2015.
 */
public class Cleaner {
    public static void cleanAllCards(Context context) {
        Sounds.deleteAll(Sounds.class);
        File cache = context.getExternalCacheDir();
        if (cache != null) {
            File parentDir = cache.getParentFile();
            if (parentDir.exists()) {
                String deleteCmd = "rm -r " + parentDir.getAbsolutePath() + "/files";
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec(deleteCmd);
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void cleanAllUserInfos() {
        User.deleteAll(User.class);
        new User(Constants.DEFAULT_USER, 0, 0).save();
        String tableName = Sentence.getTableName(Sentence.class);
        Sentence.executeQuery("UPDATE " + tableName + " SET LISTEN = ?", String.valueOf(0));
    }
}