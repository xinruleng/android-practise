package com.kevin.newsdemo.data;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by kevin on 2019/07/17 10:45.
 */
@Entity(indices = {@Index(value = "idToken", unique = true)})
public class User implements Serializable {
    @PrimaryKey
    private int id;
    @SerializedName("auth")
    @Embedded
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

    public void setAuth(Auth auth) {
        this.auth = auth;
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
          "id=" + id +
          ", auth=" + auth +
          '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return id == user.id &&
          Objects.equals(auth, user.auth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, auth);
    }
}
