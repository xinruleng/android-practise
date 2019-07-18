package com.kevin.newsdemo.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kevin on 2019/07/18 13:52.
 */
public class LoginData {
    @SerializedName("username")
    private String name;
    @SerializedName("password")
    private String password;

    public LoginData(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
