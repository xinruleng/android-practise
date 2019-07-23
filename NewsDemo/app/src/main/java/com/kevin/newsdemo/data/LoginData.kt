package com.kevin.newsdemo.data

import com.google.gson.annotations.SerializedName

/**
 * Created by kevin on 2019/07/18 13:52.
 */
data class LoginData(@SerializedName("username")
                     private val name: String,
                     @SerializedName("password")
                     private val password: String)
