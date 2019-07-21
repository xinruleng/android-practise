package com.kevin.newsdemo.user.ui;

import android.content.Intent;
import android.view.View;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.kevin.newsdemo.R;
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.user.presenter.LoginPresenter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by kevin on 2019/07/21 14:20.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityJvmTest {
    public static final String ID = "123456";
    public static final String TOKEN = "98908989089";
    public static final String REFRESH_TOKEN = "34545234234";
    private LoginActivity loginActivity;
    @Mock
    private LoginPresenter presenter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(act -> {
            loginActivity = act;
            loginActivity.setPresenter(presenter);
        });
    }

    @After
    public void tearDown() {
//        loginActivity.setPresenter(null);
    }

    @Test
    public void should_userName_and_password_isEmpty() {
        assertEquals(loginActivity.mUserName.getText().toString(), "");
        assertEquals(loginActivity.mPassword.getText().toString(), "");
    }

    @Test
    public void should_start_UserInfoActivity_when_login_succeed() {
        final String NAME = "valid";
        final String PASSWORD = "valid";

        loginActivity.mUserName.setText(NAME);
        loginActivity.mPassword.setText(PASSWORD);

        loginActivity.findViewById(R.id.login).performClick();

        verify(presenter).login(NAME, PASSWORD);


    }

    @Test
    public void should_showLoginSucceed() {
        final String NAME = "valid";
        final String PASSWORD = "valid";

        loginActivity.mUserName.setText(NAME);
        loginActivity.mPassword.setText(PASSWORD);

        loginActivity.findViewById(R.id.login).performClick();

        BaseResult<User> result = BaseResult.succeed(new User(new Auth(ID, TOKEN, REFRESH_TOKEN)));
        loginActivity.showLoginSucceed(result);

        final String latestToast = ShadowToast.getTextOfLatestToast();
        Assert.assertTrue(latestToast.startsWith("Login succeed"));

        Intent expectedIntent = new Intent(loginActivity, UserInfoActivity.class);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void should_showLoginFailed() {
        final String NAME = "valid";
        final String PASSWORD = "valid";

        loginActivity.mUserName.setText(NAME);
        loginActivity.mPassword.setText(PASSWORD);

        loginActivity.findViewById(R.id.login).performClick();

        final BaseResult result = BaseResult.error(ResultCode.INVALID_NAME_PASSWORD, new RuntimeException());
        loginActivity.showLoginFailed(result);

        final String latestToast = ShadowToast.getTextOfLatestToast();
        Assert.assertTrue(latestToast.startsWith("Login failed"));
    }

    @Test
    public void should_showLoading() {
        Assert.assertEquals(View.GONE, loginActivity.mLoading.getVisibility());

        loginActivity.setLoading(true);
        Assert.assertEquals(View.VISIBLE, loginActivity.mLoading.getVisibility());

        loginActivity.setLoading(false);
        Assert.assertEquals(View.GONE, loginActivity.mLoading.getVisibility());

    }

}
