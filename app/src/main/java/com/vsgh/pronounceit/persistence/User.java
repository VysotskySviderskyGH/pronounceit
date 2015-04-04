package com.vsgh.pronounceit.persistence;

import com.orm.SugarRecord;

/**
 * Created by Eren on 04.04.2015.
 */
public class User extends SugarRecord<User> {
    private String username;
    private int success;
    private int unsuccessful;

    public User(){}

    public User(String username, int success, int unsuccessful) {
        this.success = success;
        this.username = username;
        this.unsuccessful = unsuccessful;
    }

    public int getUnsuccessful() {
        return unsuccessful;
    }

    public void setUnsuccessful(int unsuccessful) {
        this.unsuccessful = unsuccessful;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
