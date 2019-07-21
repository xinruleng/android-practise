package com.kevin.newsdemo.user.model;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.*;
import com.kevin.newsdemo.user.model.api.UserApi;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kevin on 2019/07/17 22:20.
 */
public class UserModelTest {
    private static final String TAG = "UserModelTest";
    private UserModel model;
    public static final String ID = "123456";
    public static final String TOKEN = "98908989089";
    public static final String REFRESH_TOKEN = "34545234234";

    public static final String PROFILE_NAME = "John Smith";
    public static final String PROFILE_GENDER = "male";
    public static final String PROFILE_AVARTAR = "http://...";

    private final User MOCK_USER = new User(new Auth(ID, TOKEN, REFRESH_TOKEN));
    private final UserProfile MOCK_USER_PROFILE = new UserProfile(new Profile(PROFILE_NAME, PROFILE_GENDER, PROFILE_AVARTAR));

    @Before
    public void init() {

    }

    @Test
    public void should_return_succeed_when_use_valid_name_and_password() throws InterruptedException {
        String name = "valid";
        String password = "valid";

        UserApi mockApi = mock(UserApi.class);
        when(mockApi.login(new LoginData(name, password))).thenReturn(Observable.just(MOCK_USER));

        model = new UserModel(mockApi);
        model.login(name, password)
          .subscribe(result -> {
                Assert.assertTrue(result.isOK());
                Assert.assertEquals(MOCK_USER, result.getData());
            },
            throwable -> {
                throwable.printStackTrace();
            }
          )
        ;
    }

    @Test
    public void should_return_invalid_when_use_invalid_name_and_password() throws InterruptedException {
        String name = "valid";
        String password = "valid1";

        UserApi mockApi = mock(UserApi.class);
        final ResponseBody body = ResponseBody.create(MediaType.get("application/json; charset=utf-8"), "");
        HttpException throwable = new HttpException(Response.error(400, body));
        when(mockApi.login(new LoginData(name, password))).thenReturn(Observable.error(throwable));

        model = new UserModel(mockApi);
        model.login(name, password)
          .subscribe(
            result -> {
                Assert.assertEquals(ResultCode.INVALID_NAME_PASSWORD, result.getCode());
            })
        ;
    }

    @Test
    public void should_return_profile_when_use_valid_id_and_token() throws Exception {
        String name = "valid";
        String password = "valid";

        UserApi mockApi = mock(UserApi.class);
        when(mockApi.login(new LoginData(name, password))).thenReturn(Observable.just(MOCK_USER));
        when(mockApi.getProfile(ID, TOKEN)).thenReturn(Observable.just(MOCK_USER_PROFILE));

        model = new UserModel(mockApi);

        model.login(name, password)
          .flatMap(result -> model.getProfile(result.getData()))
          .subscribe(result -> {
              Assert.assertTrue(result.isOK());
              Assert.assertEquals(MOCK_USER_PROFILE, result.getData());
          });
    }

    @Test
    public void should_return_error_when_use_invalid_id_and_token() throws Exception {
        String name = "valid";
        String password = "valid";

        UserApi mockApi = mock(UserApi.class);
        when(mockApi.login(new LoginData(name, password))).thenReturn(Observable.just(MOCK_USER));
        final ResponseBody body = ResponseBody.create(MediaType.get("application/json; charset=utf-8"), "");
        HttpException throwable = new HttpException(Response.error(400, body));
        when(mockApi.getProfile(ID, TOKEN)).thenReturn(Observable.error(throwable));

        model = new UserModel(mockApi);

        model.login(name, password)
          .flatMap(result -> model.getProfile(result.getData()))
          .subscribe(result -> {
              Assert.assertFalse(result.isOK());
              Assert.assertEquals(ResultCode.TOKEN_ERROR, result.getCode());
          });
    }
}
