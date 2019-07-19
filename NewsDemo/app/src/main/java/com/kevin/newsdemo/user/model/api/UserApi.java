package com.kevin.newsdemo.user.model.api;

import com.kevin.newsdemo.data.LoginData;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by kevin on 2019/07/18 13:36.
 */
public interface UserApi {
    @POST("/user/login")
    Call<User> login(@Body LoginData loginData);

    @GET("/user/profile")
    Observable<UserProfile> getProfile(@Header("id-token") String id, @Header("access-token") String token);
}
