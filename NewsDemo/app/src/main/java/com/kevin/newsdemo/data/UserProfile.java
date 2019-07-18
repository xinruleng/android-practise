package com.kevin.newsdemo.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kevin on 2019/07/17 21:57.
 */
public class UserProfile {
    @SerializedName("profile")
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }
}
