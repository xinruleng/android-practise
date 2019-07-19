package com.kevin.newsdemo.data;

import androidx.room.ColumnInfo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by kevin on 2019/07/17 13:29.
 */
public class Auth implements Serializable {
    @SerializedName("idToken-token")
    @ColumnInfo(name = "idToken")
    private String idToken;

    @SerializedName("access-token")
    @ColumnInfo(name = "accessToken")
    private String token;

    @ColumnInfo(name = "refreshToken")
    @SerializedName("refresh-token")
    private String refreshToken;

    public Auth() {
    }

    public Auth(String idToken, String token, String refreshToken) {
        this.idToken = idToken;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
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
                "idToken='" + idToken + '\'' +
                ", token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Auth))
            return false;
        Auth auth = (Auth) o;
        return Objects.equals(idToken, auth.idToken) &&
                Objects.equals(token, auth.token) &&
                Objects.equals(refreshToken, auth.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idToken, token, refreshToken);
    }
}
