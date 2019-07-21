package com.kevin.newsdemo.data;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LoginData))
            return false;
        LoginData loginData = (LoginData) o;
        return Objects.equals(name, loginData.name) &&
          Objects.equals(password, loginData.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }
}
