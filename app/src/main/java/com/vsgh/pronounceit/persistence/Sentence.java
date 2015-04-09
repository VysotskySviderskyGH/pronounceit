package com.vsgh.pronounceit.persistence;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Slawa on 3/12/2015.
 */
public class Sentence extends SugarRecord<Sentence> {
    private Long id;
    private String sentence;
    private String link;
    private int lessonId;

    public Sentence() {
    }

    public Sentence(Long id, String sentence, String link, int lessonId) {
        this.id = id;
        this.sentence = sentence;
        this.link = link;
        this.lessonId = lessonId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }
}
