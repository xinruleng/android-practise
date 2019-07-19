package com.kevin.newsdemo.user.model;

import com.kevin.newsdemo.base.CallBack;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.LoginData;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import com.kevin.newsdemo.user.model.api.ApiClient;
import com.kevin.newsdemo.user.model.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.HttpURLConnection;

/**
 * Created by kevin on 2019/07/17 10:40.
 */
public class UserModel {
    private static final String TAG = "UserModel";
    public static final String HOST = "http://10.0.0.35:12306";

    private User mUser;
    private UserProfile mUserProfile;

    public void login(final String name, final String password, final CallBack<User> callBack) {
//        Thread.sleep(SLEEP_MILLIS);
        final UserApi userApi = ApiClient.getInstance().newApi(UserApi.class);
        final Call<User> call = userApi.login(new LoginData(name, password));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> resp) {
                if (resp.code() == HttpURLConnection.HTTP_OK) {
                    mUser = resp.body();
                    callBack.onSuccess(resp.body());
                }
                else {
                    mUser = null;
                    callBack.onFailed(ResultCode.INVALID_NAME_PASSWORD, new RuntimeException());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mUser = null;
                callBack.onFailed(ResultCode.ERROR, t);
            }
        });
    }

    public void getProfile(final User user, final CallBack<UserProfile> callBack) {
//        Thread.sleep(SLEEP_MILLIS);
        final UserApi userApi = ApiClient.getInstance().newApi(UserApi.class);
        final Call<UserProfile> call = userApi.getProfile(user.getAuth().getId(), user.getAuth().getToken());
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> resp) {
                if (resp.code() == HttpURLConnection.HTTP_OK) {
                    mUserProfile = resp.body();
                    callBack.onSuccess(resp.body());
                }
                else {
                    mUserProfile = null;
                    callBack.onFailed(ResultCode.TOKEN_ERROR, new RuntimeException());
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                mUserProfile = null;
                callBack.onFailed(ResultCode.ERROR, t);
            }
        });
    }

    public User getUser() {
        return mUser;
    }

    public static final int SLEEP_MILLIS = 2;
}
