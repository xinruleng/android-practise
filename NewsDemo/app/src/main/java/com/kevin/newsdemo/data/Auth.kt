package com.kevin.newsdemo.data

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by kevin on 2019/07/17 13:29.
 */
class Auth : Serializable {
    @SerializedName("id-token")
    @ColumnInfo(name = "idToken")
    var idToken: String? = null

    @SerializedName("access-token")
    @ColumnInfo(name = "accessToken")
    var token: String? = null

    @ColumnInfo(name = "refreshToken")
    @SerializedName("refresh-token")
    var refreshToken: String? = null

    constructor() {}

    constructor(idToken: String, token: String, refreshToken: String) {
        this.idToken = idToken
        this.token = token
        this.refreshToken = refreshToken
    }

    override fun toString(): String {
        return "Auth{" +
                "idToken='" + idToken + '\''.toString() +
                ", token='" + token + '\''.toString() +
                ", refreshToken='" + refreshToken + '\''.toString() +
                '}'.toString()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o)
            return true
        if (o !is Auth)
            return false
        val auth = o as Auth?
        return idToken == auth!!.idToken &&
                token == auth.token &&
                refreshToken == auth.refreshToken
    }

    override fun hashCode(): Int {
        return Objects.hash(idToken, token, refreshToken)
    }
}
