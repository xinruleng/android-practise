package com.kevin.newsdemo.user.model;

import android.util.Log;
import com.google.gson.Gson;
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.CallBack;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
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
    private static final String HOST = "http://192.168.1.194:12306";

    private User mUser;
    private UserProfile mUserProfile;

    public void login(final String name, final String password, final CallBack<User> callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    final BaseResult<User> result = doLogin(name, password);
                    if (result.isOK()) {
                        mUser = result.getData();
                        callBack.onSuccess(result.getData());
                    }
                    else {
                        mUser = null;
                        callBack.onFailed(ResultCode.INVALID_NAME_PASSWORD, new RuntimeException());
                    }
                }
                catch (Exception e) {
                    mUser = null;
                    callBack.onFailed(ResultCode.ERROR, e);
                }
            }
        }).start();

    }

    public void getProfile(final User user, final CallBack<UserProfile> callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    BaseResult<UserProfile> result = doGetProfile(user.getAuth().getId(), user.getAuth().getToken());
                    if (result.isOK()) {
                        mUserProfile = result.getData();
                        callBack.onSuccess(result.getData());
                    }
                    else {
                        mUserProfile = null;
                        callBack.onFailed(ResultCode.ERROR, new RuntimeException());
                    }
                }
                catch (Exception e) {
                    mUserProfile = null;
                    callBack.onFailed(ResultCode.ERROR, e);
                }
            }
        }).start();
    }

    BaseResult<UserProfile> doGetProfile(String id, String token) throws Exception {
        Log.d(TAG, "doGetProfile: ");
        try {
            URL url = new URL(HOST + "/user/profile");
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10_000);
                connection.setReadTimeout(10_000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("id-token", id);
                connection.setRequestProperty("access-token", token);

                int code = connection.getResponseCode();
                Log.d(TAG, "doGetProfile: code=" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    final InputStream inputStream = connection.getInputStream();
                    String content = IOUtils.getContent(inputStream);
                    Log.d(TAG, "doGetProfile: content=" + content);
                    UserProfile data = new Gson().fromJson(content, UserProfile.class);
                    return BaseResult.succeed(data);
                }

                return BaseResult.error(ResultCode.TOKEN_ERROR, new RuntimeException());
            }
            catch (IOException e) {
                Log.e(TAG, "doGetProfile: error: ", e);
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

    public User getUser() {
        return mUser;
    }

    BaseResult<User> doLogin(String name, String password) throws Exception {
        Log.d(TAG, "doLogin: name=" + name);
        try {
            URL url = new URL(HOST + "/user/login");
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10_000);
                connection.setReadTimeout(10_000);
                connection.setRequestMethod("POST");
                Log.d(TAG, "doLogin: getDoOutput=" + connection.getDoOutput() + ",getDoInput=" + connection.getDoInput());
                connection.setDoOutput(true);
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
                    User user = new Gson().fromJson(content, User.class);
                    return BaseResult.succeed(user);
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
