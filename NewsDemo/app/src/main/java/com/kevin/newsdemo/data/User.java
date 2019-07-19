package com.kevin.newsdemo.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kevin on 2019/07/17 10:45.
 */
public class User implements Serializable {
    private int id;
    @SerializedName("auth")
    private Auth auth;

    public User() {
    }

    public User(Auth auth) {
        this.auth = auth;
    }

    public User(int id, Auth auth) {
        this.id = id;
        this.auth = auth;
    }

    public Auth getAuth() {
        return auth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "auth=" + auth +
                '}';
    }
}
