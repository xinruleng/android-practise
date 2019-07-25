package com.kevin.newsdemo.data

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by kevin on 2019/07/17 13:29.
 */
data class Auth( @SerializedName("id-token")
                 @ColumnInfo(name = "idToken")
                 var idToken: String,

                 @SerializedName("access-token")
                 @ColumnInfo(name = "accessToken")
                 var token: String,

                 @ColumnInfo(name = "refreshToken")
                 @SerializedName("refresh-token")
                 var refreshToken: String) : Serializable
