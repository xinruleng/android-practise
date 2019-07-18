package com.kevin.newsdemo.user.model.api;

import com.kevin.newsdemo.user.model.UserModel;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        final Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(UserModel.HOST)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

    }

    public <T> T newApi(Class<T> service) {
        return retrofit.create(service);
    }
}
