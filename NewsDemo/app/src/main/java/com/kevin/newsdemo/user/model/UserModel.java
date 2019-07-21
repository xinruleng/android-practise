package com.kevin.newsdemo.user.model;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.LoginData;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import com.kevin.newsdemo.user.model.api.UserApi;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

import java.net.HttpURLConnection;

/**
 * Created by kevin on 2019/07/17 10:40.
 */
public class UserModel {
    private static final String TAG = "UserModel";
    public static final String HOST = "http://192.168.1.194:12306";

    private User mUser;
    private UserProfile mUserProfile;

    private UserApi mUserApi;

    public UserModel(UserApi mUserApi) {
        this.mUserApi = mUserApi;
    }

    public Observable<BaseResult<User>> login(final String name, final String password) {
//        Thread.sleep(SLEEP_MILLIS);
        return mUserApi.login(new LoginData(name, password))
          .map(user -> BaseResult.succeed(user))
          .onErrorReturn(new Function<Throwable, BaseResult<User>>() {
              @Override
              public BaseResult<User> apply(Throwable t) throws Exception {
                  HttpException exception = (HttpException) t;
                  int code = exception.code();
                  if (code == HttpURLConnection.HTTP_BAD_REQUEST) {
                      return BaseResult.error(ResultCode.INVALID_NAME_PASSWORD, t);
                  }
                  return BaseResult.error(ResultCode.ERROR, t);
              }
          })
          ;
    }

    public Observable<BaseResult<UserProfile>> getProfile(final User user) {
//        Thread.sleep(SLEEP_MILLIS);
        Observable<UserProfile> observable = mUserApi.getProfile(user.getAuth().getIdToken(), user.getAuth().getToken());

        return observable.map(profile -> {
            return BaseResult.succeed(profile);
        }).onErrorReturn(new Function<Throwable, BaseResult<UserProfile>>() {
            @Override
            public BaseResult<UserProfile> apply(Throwable t) throws Exception {
                HttpException exception = (HttpException) t;
                int code = exception.code();
                if (code == HttpURLConnection.HTTP_BAD_REQUEST) {
                    return BaseResult.error(ResultCode.TOKEN_ERROR, t);
                }
                return BaseResult.error(ResultCode.ERROR, t);
            }
        });

    }

    public User getUser() {
        return mUser;
    }

    public static final int SLEEP_MILLIS = 2;
}
