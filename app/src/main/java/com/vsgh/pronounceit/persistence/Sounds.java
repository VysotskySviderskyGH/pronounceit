package com.vsgh.pronounceit.persistence;

import com.orm.SugarRecord;

/**
 * Created by Eren on 16.03.2015.
 */
public class Sounds extends SugarRecord<Sounds> {
    private String name;
    private Boolean downloaded;

    public Sounds() {
    }

    public Sounds(String name, Boolean downloaded) {

        this.name = name;
        this.downloaded = downloaded;
    }

    public String getName() {
        return name;
    }

    public Boolean getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        this.downloaded = downloaded;
    }
}
