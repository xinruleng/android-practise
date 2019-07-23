package com.kevin.newsdemo.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by kevin on 2019/07/17 10:45.
 */
@Entity(indices = [Index(value = ["idToken"], unique = true)])
class User : Serializable {
    @PrimaryKey
    var id: Int = 0
    @SerializedName("auth")
    @Embedded
    lateinit var auth: Auth

    constructor() {}

    constructor(auth: Auth) {
        this.auth = auth
    }

    constructor(id: Int, auth: Auth) {
        this.id = id
        this.auth = auth
    }

    override fun toString(): String {
        return "User{" +
                "id=" + id +
                ", auth=" + auth +
                '}'.toString()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o)
            return true
        if (o !is User)
            return false
        val user = o as User?
        return id == user!!.id && auth == user.auth
    }

    override fun hashCode(): Int {
        return Objects.hash(id, auth)
    }
}
