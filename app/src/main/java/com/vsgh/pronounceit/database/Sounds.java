package com.vsgh.pronounceit.database;

import com.orm.SugarRecord;

/**
 * Created by Eren on 16.03.2015.
 */
public class Sounds extends SugarRecord<Sounds> {
    String name;


    public String getName() {
        return name;
    }
    public Sounds(){
    }
    public Sounds(String name) {
        this.name = name;
    }
}
