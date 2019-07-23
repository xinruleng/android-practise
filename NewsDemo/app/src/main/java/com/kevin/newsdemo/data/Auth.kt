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
    lateinit var idToken: String

    @SerializedName("access-token")
    @ColumnInfo(name = "accessToken")
    lateinit var token: String

    @ColumnInfo(name = "refreshToken")
    @SerializedName("refresh-token")
    lateinit var refreshToken: String

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
