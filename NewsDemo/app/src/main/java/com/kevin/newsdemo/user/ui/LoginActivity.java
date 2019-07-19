package com.kevin.newsdemo.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.test.espresso.idling.CountingIdlingResource;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kevin.newsdemo.R;
import com.kevin.newsdemo.base.BaseActivity;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.user.model.UserModel;
import com.kevin.newsdemo.utils.ViewUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.io.Serializable;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "UserLoginActivity";

    @BindView(R.id.username)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.loading)
    View mLoading;

    private CountingIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

//        mUserName.setText("valid");
//        mPassword.setText("valid");

    }

    @OnClick(R.id.login)
    public void login() {
        Log.d(TAG, "login: ");

        loading(true);

        UserModel userModel = new UserModel();
        String name = ViewUtils.getText(mUserName);
        String password = ViewUtils.getText(mPassword);

        getIdlingResource().increment();
        userModel.login(name, password)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
            o -> {
                loading(false);
                getIdlingResource().decrement();
                toast("登录成功 " + o);
                startUserInfoActivity(o);
            },
            t -> {
                loading(false);
                getIdlingResource().decrement();
                toast("登录失败 " + t);
            }
          )
        ;

//        loading(false);
//        User user = new User(new Auth("1", "token", "token2"));
//        startUserInfoActivity(user);
    }

    private void startUserInfoActivity(User user) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra(UserInfoActivity.USER, (Serializable) user);
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

    public CountingIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new CountingIdlingResource("CountingIdlingResource");
        }
        return mIdlingResource;
    }
}
