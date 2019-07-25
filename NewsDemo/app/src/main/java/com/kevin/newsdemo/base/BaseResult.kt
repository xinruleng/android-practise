package com.kevin.newsdemo.base

import java.util.*

/**
 * Created by kevin on 2019/07/17 13:17.
 */
class BaseResult<T>(var code: ResultCode, var data:T?, var throwable: Throwable?) {
    val isOK: Boolean
        get() = code === ResultCode.OK

    private constructor(code: ResultCode, t: T) : this(code,t,null) {
        this.code = code
        this.data = t
    }

    private constructor(code: ResultCode, throwable: Throwable) :this(code,null,throwable) {
        this.code = code
        this.throwable = throwable
    }

    override fun equals(o: Any?): Boolean {
        if (this === o)
            return true
        if (o !is BaseResult<*>)
            return false
        val that = o as BaseResult<*>?
        return code === that!!.code &&
                data == that!!.data &&
                throwable == that.throwable
    }

    override fun hashCode(): Int {
        return Objects.hash(code, data, throwable)
    }

    companion object {

        fun <T> succeed(t: T): BaseResult<T> {
            return BaseResult(ResultCode.OK, t)
        }

        fun error(t: Throwable): BaseResult<Any> {
            return BaseResult(ResultCode.ERROR, t)
        }

        fun error(code: ResultCode, t: Throwable): BaseResult<Any> {
            return BaseResult(code, t)
        }
    }
}
