package com.kevin.newsdemo.data;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by kevin on 2019/07/17 21:57.
 */
public class UserProfile {
    @SerializedName("profile")
    private Profile profile;

    public UserProfile() {
    }

    public UserProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserProfile))
            return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(profile, that.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile);
    }
}
