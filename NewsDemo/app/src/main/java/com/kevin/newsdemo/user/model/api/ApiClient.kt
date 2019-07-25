package com.kevin.newsdemo.user.model.api

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.kevin.newsdemo.BuildConfig
import com.kevin.newsdemo.user.model.UserModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by kevin on 2019/07/18 13:38.
 */
class ApiClient private constructor() {
    private val retrofit: Retrofit

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addNetworkInterceptor(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        Log.d(TAG, "intercept: " + Thread.currentThread().name)
                        if (BuildConfig.DEBUG) {
                            try {
                                Thread.sleep(1000)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }

                        }
                        return chain.proceed(chain.request())
                    }
                })
                .build()
        val builder = Retrofit.Builder()
                .baseUrl(UserModel.HOST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        retrofit = builder.build()

    }

    fun <T> newApi(service: Class<T>): T {
        return retrofit.create(service)
    }

    companion object {
        private val TAG = "ApiClient"
        val instance = ApiClient()
    }
}
