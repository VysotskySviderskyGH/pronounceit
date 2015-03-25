package com.vsgh.pronounceit.singletones;

import com.vsgh.pronounceit.entity.Sentence;

import java.util.List;

/**
 * Created by Slawa on 3/12/2015.
 */
public class SentenceContainer {
    public static boolean isInit = false;
    public static List<Sentence> sentences;

    private SentenceContainer() {
    }
}
