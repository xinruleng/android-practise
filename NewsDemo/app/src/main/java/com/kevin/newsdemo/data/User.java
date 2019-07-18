package com.kevin.newsdemo.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kevin on 2019/07/17 10:45.
 */
public class User implements Serializable {
    @SerializedName("auth")
    private Auth auth;

    public Auth getAuth() {
        return auth;
    }

    @Override
    public String toString() {
        return "User{" +
                "auth=" + auth +
                '}';
    }
}
