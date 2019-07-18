package com.kevin.newsdemo.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.kevin.newsdemo.R;
import com.kevin.newsdemo.base.BaseActivity;
import com.kevin.newsdemo.base.CallBack;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.user.model.UserModel;
import com.kevin.newsdemo.utils.ViewUtils;

import java.io.Serializable;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "UserLoginActivity";

    private EditText mUserName;
    private EditText mPassword;
    private Button mLogin;

    private View mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserName = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLoading = findViewById(R.id.loading);

        mLogin = findViewById(R.id.login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mUserName.setText("valid");
        mPassword.setText("valid");

    }

    private void login() {
        Log.d(TAG, "login: ");

        loading(true);

        UserModel userModel = new UserModel();
        String name = ViewUtils.getText(mUserName);
        String password = ViewUtils.getText(mPassword);

        userModel.login(name, password, new CallBack<User>() {
            @Override
            public void onSuccess(User o) {
                loading(false);
                toast("登录成功 " + o);
                startUserInfoActivity(o);
            }

            @Override
            public void onFailed(ResultCode code, Throwable throwable) {
                loading(false);
                toast("登录失败 " + code.getDes() + "," + throwable);
            }
        });
    }

    private void startUserInfoActivity(User user) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra(UserInfoActivity.USER,(Serializable) user);
        startActivity(intent);
    }

    private void loading(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoading.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
