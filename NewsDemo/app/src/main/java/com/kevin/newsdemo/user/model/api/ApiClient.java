package com.kevin.newsdemo.user.model.api;

import android.util.Log;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kevin.newsdemo.BuildConfig;
import com.kevin.newsdemo.user.model.UserModel;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 2019/07/18 13:38.
 */
public class ApiClient {
    private static final String TAG = "ApiClient";
    private static final ApiClient ourInstance = new ApiClient();
    private Retrofit retrofit;

    public static ApiClient getInstance() {
        return ourInstance;
    }

    private ApiClient() {
        OkHttpClient client = new OkHttpClient.Builder()
          .connectTimeout(5, TimeUnit.SECONDS)
          .readTimeout(5, TimeUnit.SECONDS)
          .addNetworkInterceptor(new Interceptor() {
              @Override
              public Response intercept(Chain chain) throws IOException {
                  Log.d(TAG, "intercept: " + Thread.currentThread().getName());
                  if (BuildConfig.DEBUG) {
                            try {
                                Thread.sleep(1000);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                  }
                  return chain.proceed(chain.request());
              }
          })
          .build();
        final Retrofit.Builder builder = new Retrofit.Builder()
          .baseUrl(UserModel.HOST)
          .client(client)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        retrofit = builder.build();

    }

    public <T> T newApi(Class<T> service) {
        return retrofit.create(service);
    }
}
