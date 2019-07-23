package com.kevin.newsdemo.user.model

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.base.ResultCode
import com.kevin.newsdemo.data.LoginData
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.data.UserProfile
import com.kevin.newsdemo.user.model.api.UserApi
import io.reactivex.Observable
import io.reactivex.functions.Function

import java.net.HttpURLConnection

/**
 * Created by kevin on 2019/07/17 10:40.
 */
class UserModel(private val mUserApi: UserApi) {

    val user: User? = null
    private val mUserProfile: UserProfile? = null

    fun login(name: String, password: String): Observable<BaseResult<User>> {
        //        Thread.sleep(SLEEP_MILLIS);
        return mUserApi.login(LoginData(name, password))
                .map { user -> BaseResult.succeed(user) }
                .onErrorReturn(Function<Throwable, BaseResult<User>> { t ->
                    val exception = t as HttpException
                    val code = exception.code()
                    (if (code == HttpURLConnection.HTTP_BAD_REQUEST) {
                        BaseResult.error(ResultCode.INVALID_NAME_PASSWORD, t)
                    } else {
                        BaseResult.error(ResultCode.ERROR, t)
                    }) as BaseResult<User>
                })
    }

    fun getProfile(user: User): Observable<BaseResult<UserProfile>> {
        //        Thread.sleep(SLEEP_MILLIS);
        val observable = mUserApi.getProfile(user.auth.idToken, user.auth!!.token)

        return observable.map { profile -> BaseResult.succeed(profile) }.onErrorReturn(Function<Throwable, BaseResult<UserProfile>> { t ->
            val exception = t as HttpException
            val code = exception.code()
            (if (code == HttpURLConnection.HTTP_BAD_REQUEST) {
                BaseResult.error(ResultCode.TOKEN_ERROR, t)
            } else BaseResult.error(ResultCode.ERROR, t)) as BaseResult<UserProfile>
        })

    }

    companion object {
        private val TAG = "UserModel"
        val HOST = "http://10.0.0.35:12306"

        val SLEEP_MILLIS = 2
    }
}
