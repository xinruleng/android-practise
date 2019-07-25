package com.kevin.newsdemo.base

/**
 * Created by kevin on 2019/07/17 10:41.
 */
interface CallBack<T> {

    fun onSuccess(t: T)

    fun onFailed(code: ResultCode, throwable: Throwable)
}
