package com.kevin.newsdemo.user.model.api;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kevin.newsdemo.data.*;
import com.kevin.newsdemo.user.model.UserModel;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
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

    public static final String LOGIN_SUCCEED_RESPONSE = "{\n" +
      "    \"auth\": {\n" +
      "        \"id-token\": \"123456\",\n" +
      "        \"access-token\": \"98908989089\",\n" +
      "        \"refresh-token\": \"34545234234\"\n" +
      "    }\n" +
      "}";


    public static final String PROFILE_SUCCEED_RESPONSE = "{\n" +
      "    \"profile\": {\n" +
      "        \"name\": \"John Smith\",\n" +
      "        \"gender\": \"male\",\n" +
      "        \"avartar\": \"http://...\"\n" +
      "    }\n" +
      "}";

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
          .baseUrl(UserModel.Companion.getHOST())
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

        mUserApi = mockApi(LOGIN_SUCCEED_RESPONSE);

        final Observable<User> observable = mUserApi.login(new LoginData(name, password));
        TestObserver testObserver = new TestObserver();
        observable.subscribe(testObserver);

        testObserver.assertValue(MOCK_USER);
    }

    @Test
    public void should_return_invalid_when_use_invalid_name_and_password() {
        String name = "valid";
        String password = "valid";
        mUserApi = mockApi(400, "");

        final Observable<User> observable = mUserApi.login(new LoginData(name, password));
        TestObserver testObserver = new TestObserver();
        observable.subscribe(testObserver);

        testObserver.assertError(throwable -> {
            Assert.assertTrue(throwable instanceof HttpException);
            HttpException httpException = (HttpException) throwable;
            Assert.assertEquals(400, httpException.code());
            return true;
        });
    }

    @Test
    public void should_return_profile_when_use_valid_id_and_token() throws Exception {
        mUserApi = mockApi(PROFILE_SUCCEED_RESPONSE);

        final Observable<UserProfile> observable = mUserApi.getProfile(ID, TOKEN);
        TestObserver testObserver = new TestObserver();
        observable.subscribe(testObserver);

        testObserver.assertValue(MOCK_USER_PROFILE);
    }

    @Test
    public void should_return_error_when_use_invalid_id_and_token() throws Exception {
        mUserApi = mockApi(400, "");

        final Observable<UserProfile> observable = mUserApi.getProfile(ID, TOKEN);
        TestObserver testObserver = new TestObserver();
        observable.subscribe(testObserver);
        testObserver.assertError(throwable -> {
            Assert.assertTrue(throwable instanceof HttpException);
            HttpException httpException = (HttpException) throwable;
            Assert.assertEquals(400, httpException.code());
            return true;
        });
    }
}
