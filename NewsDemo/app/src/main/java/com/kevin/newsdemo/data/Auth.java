package com.kevin.newsdemo.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kevin on 2019/07/17 13:29.
 */
public class Auth implements Serializable {
    @SerializedName("id-token")
    private String id;
    @SerializedName("access-token")
    private String token;
    @SerializedName("refresh-token")
    private String refreshToken;

    public Auth() {
    }

    public Auth(String id, String token, String refreshToken) {
        this.id = id;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
