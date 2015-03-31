package com.vsgh.pronounceit.persistence;

import com.orm.SugarRecord;

/**
 * Created by Eren on 16.03.2015.
 */
public class Sounds extends SugarRecord<Sounds> {
    String name;

    public Sounds() {
    }

    public Sounds(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
