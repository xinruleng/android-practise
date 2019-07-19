package com.kevin.newsdemo.user.model;

import android.util.Log;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.kevin.newsdemo.base.CallBack;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.Profile;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/**
 * Created by kevin on 2019/07/17 22:20.
 */
public class UserModelTest {
    private static final String TAG = "UserModelTest";
    private UserModel model;
    private CountDownLatch mutex;


    @Before
    public void init() {
        model = new UserModel();
        mutex = new CountDownLatch(1);
    }

    @Test
    public void should_return_succeed_when_use_valid_name_and_password() throws InterruptedException {
        String name = "valid";
        String password = "valid";

        model.login(name, password, new CallBack<User>() {
            @Override
            public void onSuccess(User user) {
                Auth auth = user.getAuth();
                assertEquals("123456", auth.getIdToken());
                assertEquals("98908989089", auth.getToken());
                assertEquals("34545234234", auth.getRefreshToken());
                mutex.countDown();
            }

            @Override
            public void onFailed(ResultCode code, Throwable throwable) {
                mutex.countDown();
            }
        });
        mutex.await();
    }

    @Test
    public void should_return_invalid_when_use_invalid_name_and_password() throws InterruptedException {
        String name = "valid";
        String password = "valid1";
        model.login(name, password, new CallBack<User>() {
            @Override
            public void onSuccess(User user) {
                mutex.countDown();
            }

            @Override
            public void onFailed(ResultCode code, Throwable throwable) {
                Assert.assertEquals(ResultCode.INVALID_NAME_PASSWORD, code);
                mutex.countDown();
            }
        });
        mutex.await();
    }

    @Test
    public void should_return_profile_when_use_valid_id_and_token() throws Exception {
        String name = "valid";
        String password = "valid";
        model.login(name, password, new CallBack<User>() {
            @Override
            public void onSuccess(User user) {
                model.getProfile(user).subscribe(
                        userProfile -> {
                            checkUserProfile(userProfile);
                            mutex.countDown();
                        });
            }

            @Override
            public void onFailed(ResultCode code, Throwable throwable) {
                mutex.countDown();
            }
        });
        mutex.await();
    }

    private void checkUserProfile(UserProfile userProfile) {
        Profile profile = userProfile.getProfile();
        assertEquals("John Smith", profile.getName());
        assertEquals("male", profile.getGender());
        assertEquals("", profile.getAvartar());
    }

    @Test
    public void should_return_error_when_use_invalid_id_and_token() throws Exception {
        String name = "valid";
        String password = "valid";
        model.login(name, password, new CallBack<User>() {
            @Override
            public void onSuccess(User user) {
                final Auth auth = user.getAuth();
                auth.setIdToken(auth.getIdToken() + 1);
                auth.setToken(auth.getToken() + 1);
                model.getProfile(user)
                        .subscribe(
                                userProfile -> mutex.countDown(),
                                t -> {
                                    HttpException exception = (HttpException) t;
                                    int code = exception.code();
                                    Assert.assertEquals(400, code);
                                    Log.d(TAG, "onSuccess: t");
                                    mutex.countDown();
                                }
                        )
                ;
            }

            @Override
            public void onFailed(ResultCode code, Throwable throwable) {
                mutex.countDown();
            }
        });
        mutex.await();
    }
}
