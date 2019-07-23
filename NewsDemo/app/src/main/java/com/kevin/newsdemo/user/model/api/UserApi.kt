package com.kevin.newsdemo.user.model.api

import com.kevin.newsdemo.data.LoginData
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.data.UserProfile
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by kevin on 2019/07/18 13:36.
 */
interface UserApi {
    @POST("/user/login")
    fun login(@Body loginData: LoginData): Observable<User>

    @GET("/user/profile")
    fun getProfile(@Header("id-token") id: String, @Header("access-token") token: String): Observable<UserProfile>
}
