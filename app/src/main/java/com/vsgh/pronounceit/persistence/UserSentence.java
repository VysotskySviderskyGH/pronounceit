package com.vsgh.pronounceit.persistence;

import com.orm.SugarRecord;

/**
 * Created by Eren on 09.04.2015.
 */
public class UserSentence extends SugarRecord<UserSentence> {
    private String username;
    private String sentence;

    public UserSentence(){}

    public UserSentence(String username, String sentence) {
        this.username = username;
        this.sentence = sentence;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
