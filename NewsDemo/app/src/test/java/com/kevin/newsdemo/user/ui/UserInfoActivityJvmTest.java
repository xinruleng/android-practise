package com.kevin.newsdemo.user.ui;

import android.content.Intent;
import android.view.View;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.Profile;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import com.kevin.newsdemo.user.presenter.ProfilePresenter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by kevin on 2019/07/21 15:31.
 */
@RunWith(AndroidJUnit4.class)
public class UserInfoActivityJvmTest {
    public static final String ID = "123456";
    public static final String TOKEN = "98908989089";
    public static final String REFRESH_TOKEN = "34545234234";

    public static final String PROFILE_NAME = "John Smith";
    public static final String PROFILE_GENDER = "male";
    public static final String PROFILE_AVARTAR = "http://...";
    private UserInfoActivity userInfoActivity;

    @Mock
    private ProfilePresenter presenter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UserInfoActivity.class);
        User user = new User(new Auth(ID, TOKEN, REFRESH_TOKEN));
        intent.putExtra(UserInfoActivity.USER, (Serializable) user);
        ActivityScenario<UserInfoActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(act -> {
            userInfoActivity = act;
            userInfoActivity.setPresenter(presenter);
        });
    }

    @Test
    public void should_showUserId_and_Token() {
        assertEquals("id: " + ID, userInfoActivity.mIdText.getText().toString());
        assertEquals("token: " + TOKEN, userInfoActivity.mTokenText.getText().toString());
    }

    @Test
    public void should_userName_and_password_isEmpty() {
        assertEquals("name", userInfoActivity.mNameText.getText().toString());
        assertEquals("gender", userInfoActivity.mGenderText.getText().toString());
    }

    @Test
    public void should_showLoading() {
        Assert.assertEquals(View.VISIBLE, userInfoActivity.mLoading.getVisibility());

        userInfoActivity.setLoading(false);
        Assert.assertEquals(View.GONE, userInfoActivity.mLoading.getVisibility());
    }

    @Test
    public void should_showProfileSucceed() {
        final BaseResult<UserProfile> result = BaseResult.succeed(new UserProfile(new Profile(PROFILE_NAME, PROFILE_GENDER, PROFILE_AVARTAR)));
        userInfoActivity.showProfileSucceed(result);

        assertEquals("name: " + PROFILE_NAME, userInfoActivity.mNameText.getText().toString());
        assertEquals("gender: " + PROFILE_GENDER, userInfoActivity.mGenderText.getText().toString());
    }

    @Test
    public void should_showProfileFailed() {
        BaseResult<UserProfile> result = BaseResult.error(ResultCode.TOKEN_ERROR, new RuntimeException());
        userInfoActivity.showProfileFailed(result);
    }

    @Test
    public void should_unsubscribe() {
        userInfoActivity.onPause();
        verify(presenter).unsubscribe();
    }
}
