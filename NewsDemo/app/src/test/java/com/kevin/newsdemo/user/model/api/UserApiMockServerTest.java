package com.kevin.newsdemo.user.model.api;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kevin.newsdemo.data.*;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 2019/07/20 22:37.
 */
public class UserApiMockServerTest {
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
    private MockWebServer server;

    @Before
    public void setUp() {
        server = new MockWebServer();

        Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) throws InterruptedException {
                String path = request.getPath();
                if ("/user/login".equals(path)) {
                    if (request.getBody().toString().equals("[text={\"username\":\"valid\",\"password\":\"valid\"}]")) {
                        MockResponse successResponse = new MockResponse()
                          .addHeader("Content-Type", "application/json;charset=utf-8")
                          .addHeader("Cache-Control", "no-cache")
                          .setBody(LOGIN_SUCCEED_RESPONSE);
                        return successResponse;
                    }
                    else {
                        MockResponse failedResponse = new MockResponse()
                          .addHeader("Content-Type", "application/json;charset=utf-8")
                          .setResponseCode(400)
                          .throttleBody(500, 1, TimeUnit.SECONDS) //mock slow network: 5byte per second
                          ;
                        return failedResponse;
                    }
                }
                else if ("/user/profile".equals(path)) {
                    String idToken = request.getHeader("id-token");
                    String accessToken = request.getHeader("access-token");
                    if (ID.equals(idToken) && TOKEN.equals(accessToken)) {
                        MockResponse successResponse = new MockResponse()
                          .addHeader("Content-Type", "application/json;charset=utf-8")
                          .addHeader("Cache-Control", "no-cache")
                          .setBody(PROFILE_SUCCEED_RESPONSE);
                        return successResponse;
                    }
                    else {
                        MockResponse failedResponse = new MockResponse()
                          .addHeader("Content-Type", "application/json;charset=utf-8")
                          .setResponseCode(400)
                          .throttleBody(500, 1, TimeUnit.SECONDS) //mock slow network: 5byte per second
                          ;
                        return failedResponse;
                    }

                }
                return null;
            }
        };

//        server.enqueue(mockResponse);
        server.setDispatcher(dispatcher);
    }

    @After
    public void tearDown() {
        try {
            server.shutdown();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UserApi mockApi() {
        OkHttpClient client = new OkHttpClient.Builder()
          .connectTimeout(5, TimeUnit.SECONDS)
          .readTimeout(5, TimeUnit.SECONDS)
          .build();
        final Retrofit.Builder builder = new Retrofit.Builder()
          .baseUrl("http://" + server.getHostName() + ":" + server.getPort())
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

        mUserApi = mockApi();

        final Observable<User> observable = mUserApi.login(new LoginData(name, password));
        TestObserver testObserver = new TestObserver();
        observable.subscribe(testObserver);

        testObserver.assertValue(MOCK_USER);
    }

    @Test
    public void should_return_invalid_when_use_invalid_name_and_password() {
        String name = "valid";
        String password = "valid1";
        mUserApi = mockApi();

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
        mUserApi = mockApi();

        final Observable<UserProfile> observable = mUserApi.getProfile(ID, TOKEN);
        TestObserver testObserver = new TestObserver();
        observable.subscribe(testObserver);

        testObserver.assertValue(MOCK_USER_PROFILE);
    }

    @Test
    public void should_return_error_when_use_invalid_id_and_token() throws Exception {
        mUserApi = mockApi();

        final Observable<UserProfile> observable = mUserApi.getProfile(ID, TOKEN + 1);
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
