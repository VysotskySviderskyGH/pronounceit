package com.vsgh.pronounceit.persistence;

import com.orm.SugarRecord;

/**
 * Created by Eren on 04.04.2015.
 */
public class User extends SugarRecord<User> {
    private String userName;
    private int success;
    private int unsuccessful;

    public User(){}

    public User(String userName, int success, int unsuccessful) {
        this.success = success;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
