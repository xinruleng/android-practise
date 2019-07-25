package com.kevin.newsdemo.user.model.api

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * Created by kevin on 2019/07/20 22:41.
 */
class MockInterceptor(private val code: Int, private val responseString: String) : Interceptor {

    constructor(responseString: String) : this(200, responseString) {}

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val response = Response.Builder()
                .code(code)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()

        chain.proceed(chain.request())
        return response
    }
}
