package com.kevin.newsdemo.user.model.api;

import okhttp3.*;

import java.io.IOException;

/**
 * Created by kevin on 2019/07/20 22:41.
 */
public class MockInterceptor implements Interceptor {
    private final int code;
    private final String responseString;

    public MockInterceptor(String responseString) {
        this(200, responseString);
    }

    public MockInterceptor(int code, String responseString) {
        this.code = code;
        this.responseString = responseString;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        Response response = new Response.Builder()
          .code(code)
          .message(responseString)
          .request(chain.request())
          .protocol(Protocol.HTTP_1_0)
          .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
          .addHeader("content-type", "application/json")
          .build();

        chain.proceed(chain.request());
        return response;
    }
}
