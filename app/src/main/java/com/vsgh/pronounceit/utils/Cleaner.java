package com.vsgh.pronounceit.utils;

import android.content.Context;
import android.widget.Toast;

import com.vsgh.pronounceit.persistence.Sounds;

import java.io.File;
import java.io.IOException;

/**
 * Created by Slawa on 4/4/2015.
 */
public class Cleaner {
    public static final void cleanAllCards(Context context) {
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
}