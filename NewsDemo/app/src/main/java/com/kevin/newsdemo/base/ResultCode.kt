package com.kevin.newsdemo.base

/**
 * Created by kevin on 2019/07/17 10:42.
 */
enum class ResultCode private constructor(val des: String) {
    OK("OK"),
    ERROR("ERROR"),
    INVALID_NAME_PASSWORD("invalid username or password"),
    EMPTY_NAME_OR_PASSWORD("empty name or password"),
    TOKEN_ERROR("token error")
}
