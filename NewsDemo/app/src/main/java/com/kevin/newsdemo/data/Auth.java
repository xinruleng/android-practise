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

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
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
