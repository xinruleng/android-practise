package com.kevin.newsdemo.user.model;

import android.util.Log;
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.CallBack;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kevin on 2019/07/17 10:40.
 */
public class UserModel {
    private static final String TAG = "UserModel";
    private static final String HOST = "http://10.0.0.35:12306";

    public void login(final String name, final String password, final CallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final BaseResult result = doLogin(name, password);
                    if (result.isOK()) {
                        callBack.onSuccess(new User());
                    }
                    else {
                        callBack.onFailed(ResultCode.INVALID_NAME_PASSWORD, new RuntimeException());
                    }
                }
                catch (Exception e) {
                    callBack.onFailed(ResultCode.ERROR, e);
                }
            }
        }).start();

    }

    private BaseResult doLogin(String name, String password) throws Exception {
        Log.d(TAG, "doLogin: name=" + name);
        try {
            URL url = new URL(HOST + "/user/login");
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10_000);
                connection.setReadTimeout(10_000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("username", name);
                connection.setRequestProperty("password", password);

                final OutputStream outputStream = connection.getOutputStream();
                String data = "{\n" +
                        "    \"username\": \"" + name + "\",\n" +
                        "    \"password\": \"" + password + "\"\n" +
                        "}";

                outputStream.write(data.getBytes());
                outputStream.flush();
                outputStream.close();


                int code = connection.getResponseCode();
                Log.d(TAG, "doLogin: code=" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    final InputStream inputStream = connection.getInputStream();
                    String content = IOUtils.getContent(inputStream);
                    Log.d(TAG, "doLogin: content=" + content);
                    return BaseResult.succeed(content);
                }

                return BaseResult.error(ResultCode.INVALID_NAME_PASSWORD, new RuntimeException());
            }
            catch (IOException e) {
                Log.e(TAG, "doLogin: error: ", e);
                throw e;
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

        }
        catch (MalformedURLException e) {
            Log.e(TAG, "doLogin: error: ", e);
            return BaseResult.error(e);
        }
    }
}
