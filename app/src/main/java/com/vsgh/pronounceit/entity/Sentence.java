package com.vsgh.pronounceit.entity;

/**
 * Created by Slawa on 3/12/2015.
 */
public class Sentence {
    private int id;
    private String sentence;
    private String link;
    private int lessonId;

    public Sentence(int id, String sentence, String link, int lessonId) {
        this.id = id;
        this.sentence = sentence;
        this.link = link;
        this.lessonId = lessonId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
