package com.kevin.newsdemo.user.model.api;

import com.kevin.newsdemo.user.model.UserModel;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 2019/07/18 13:38.
 */
public class ApiClient {
    private static final ApiClient ourInstance = new ApiClient();
    private Retrofit retrofit;

    public static ApiClient getInstance() {
        return ourInstance;
    }

    private ApiClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        final Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(UserModel.HOST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

    }

    public <T> T newApi(Class<T> service) {
        return retrofit.create(service);
    }
}
