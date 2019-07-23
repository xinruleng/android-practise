package com.kevin.newsdemo.data

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by kevin on 2019/07/17 21:57.
 */
class UserProfile {
    @SerializedName("profile")
    var profile: Profile? = null

    constructor() {}

    constructor(profile: Profile) {
        this.profile = profile
    }

    override fun equals(o: Any?): Boolean {
        if (this === o)
            return true
        if (o !is UserProfile)
            return false
        val that = o as UserProfile?
        return profile == that!!.profile
    }

    override fun hashCode(): Int {
        return Objects.hash(profile)
    }
}
