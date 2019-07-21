package com.kevin.newsdemo.user.model.api;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kevin.newsdemo.data.*;
import com.kevin.newsdemo.user.model.UserModel;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 2019/07/20 22:37.
 */
public class UserApiTest {
    public static final String ID = "123456";
    public static final String TOKEN = "98908989089";
    public static final String REFRESH_TOKEN = "34545234234";

    public static final String PROFILE_NAME = "John Smith";
    public static final String PROFILE_GENDER = "male";
    public static final String PROFILE_AVARTAR = "http://...";

    private final User MOCK_USER = new User(new Auth(ID, TOKEN, REFRESH_TOKEN));
    private final UserProfile MOCK_USER_PROFILE = new UserProfile(new Profile(PROFILE_NAME, PROFILE_GENDER, PROFILE_AVARTAR));

    private UserApi mUserApi;

    private UserApi mockApi(String resp) {
        return mockApi(200, resp);
    }

    private UserApi mockApi(int code, String resp) {
        OkHttpClient client = new OkHttpClient.Builder()
          .connectTimeout(5, TimeUnit.SECONDS)
          .readTimeout(5, TimeUnit.SECONDS)
          .addNetworkInterceptor(new MockInterceptor(code, resp))
          .build();
        final Retrofit.Builder builder = new Retrofit.Builder()
          .baseUrl(UserModel.HOST)
          .client(client)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        Retrofit retrofit = builder.build();

        return retrofit.create(UserApi.class);
    }

    @Test
    public void should_return_succeed_when_use_valid_name_and_password() {
        String name = "valid";
        String password = "valid";

        String json = "{\n" +
          "    \"auth\": {\n" +
          "        \"id-token\": \"123456\",\n" +
          "        \"access-token\": \"98908989089\",\n" +
          "        \"refresh-token\": \"34545234234\"\n" +
          "    }\n" +
          "}";

        mUserApi = mockApi(json);

        final Observable<User> observable = mUserApi.login(new LoginData(name, password));
        observable.subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                Assert.assertEquals(user, MOCK_USER);
            }
        });
    }

    @Test
    public void should_return_invalid_when_use_invalid_name_and_password() {
        String name = "valid";
        String password = "valid";
        mUserApi = mockApi(400, "12");

        final Observable<User> observable = mUserApi.login(new LoginData(name, password));
        observable.subscribe(u -> {
              ;
          },
          throwable -> {
              Assert.assertTrue(throwable instanceof HttpException);
              HttpException httpException = (HttpException) throwable;
              Assert.assertEquals(400, httpException.code());
              Assert.assertEquals("12", httpException.message());
          }
        );
    }

    @Test
    public void should_return_profile_when_use_valid_id_and_token() throws Exception {
        String json = "{\n" +
          "    \"profile\": {\n" +
          "        \"name\": \"John Smith\",\n" +
          "        \"gender\": \"male\",\n" +
          "        \"avartar\": \"http://...\"\n" +
          "    }\n" +
          "}";
        mUserApi = mockApi(json);

        final Observable<UserProfile> observale = mUserApi.getProfile(ID, TOKEN);
        observale.subscribe(profile->{
            Assert.assertEquals(MOCK_USER_PROFILE, profile);
        });
    }

    @Test
    public void should_return_error_when_use_invalid_id_and_token() throws Exception {
        mUserApi = mockApi(400, "12");

        final Observable<UserProfile> observable = mUserApi.getProfile(ID, TOKEN);

        observable.subscribe(u -> {
              ;
          },
          throwable -> {
              Assert.assertTrue(throwable instanceof HttpException);
              HttpException httpException = (HttpException) throwable;
              Assert.assertEquals(400, httpException.code());
              Assert.assertEquals("12", httpException.message());
          }
        );
    }
}
