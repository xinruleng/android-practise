package com.kevin.newsdemo.user.model;

import com.kevin.newsdemo.data.LoginData;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import com.kevin.newsdemo.user.model.api.ApiClient;
import com.kevin.newsdemo.user.model.api.UserApi;
import io.reactivex.Observable;

/**
 * Created by kevin on 2019/07/17 10:40.
 */
public class UserModel {
    private static final String TAG = "UserModel";
    public static final String HOST = "http://10.0.0.35:12306";

    private User mUser;
    private UserProfile mUserProfile;

    public Observable<User> login(final String name, final String password) {
//        Thread.sleep(SLEEP_MILLIS);
        final UserApi userApi = ApiClient.getInstance().newApi(UserApi.class);
        return userApi.login(new LoginData(name, password));
    }

    public Observable<UserProfile> getProfile(final User user) {
//        Thread.sleep(SLEEP_MILLIS);
        final UserApi userApi = ApiClient.getInstance().newApi(UserApi.class);
        final Observable<UserProfile> observable = userApi.getProfile(user.getAuth().getIdToken(), user.getAuth().getToken());
        return observable;
    }

    public User getUser() {
        return mUser;
    }

    public static final int SLEEP_MILLIS = 2;
}
