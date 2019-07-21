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
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.schedulers.SchedulerProvider;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.user.UserContract;
import com.kevin.newsdemo.user.model.UserModel;
import com.kevin.newsdemo.user.model.api.ApiClient;
import com.kevin.newsdemo.user.model.api.UserApi;
import com.kevin.newsdemo.user.presenter.LoginPresenter;
import com.kevin.newsdemo.utils.ViewUtils;

import java.io.Serializable;

public class LoginActivity extends BaseActivity implements UserContract.View {
    private static final String TAG = "UserLoginActivity";

    @BindView(R.id.username)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.loading)
    View mLoading;

    private CountingIdlingResource mIdlingResource;

    private UserContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        UserApi api = ApiClient.getInstance().newApi(UserApi.class);
        final UserModel userModel = new UserModel(api);
        mPresenter = new LoginPresenter(userModel, this, SchedulerProvider.getInstance());
    }

    @OnClick(R.id.login)
    public void login() {
        Log.d(TAG, "login: ");

        loading(true);

        String name = ViewUtils.getText(mUserName);
        String password = ViewUtils.getText(mPassword);

        getIdlingResource().increment();

        mPresenter.login(name, password);
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

    @Override
    public void showLoginSucceed(BaseResult<User> o) {
        getIdlingResource().decrement();
        toast("Login succeed " + o);
        startUserInfoActivity(o.getData());
    }

    @Override
    public void showLoginFailed(BaseResult<User> result) {
        getIdlingResource().decrement();
        toast("Login failed "+ result.getThrowable());
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
//        mPresenter = presenter;
    }

    @Override
    public void setLoading(boolean active) {
        loading(active);
    }
}
