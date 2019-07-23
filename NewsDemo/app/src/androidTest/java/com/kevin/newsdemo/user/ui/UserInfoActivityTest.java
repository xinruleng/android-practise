package com.kevin.newsdemo.user.ui;

import android.content.Intent;
import android.util.Log;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.kevin.newsdemo.R;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by kevin on 2019/07/18 12:14.
 */
@RunWith(AndroidJUnit4.class)
public class UserInfoActivityTest {
    private static final String TAG = "UserInfoActivity";
    @Rule
    public ActivityTestRule<UserInfoActivity> mUserInfoActivityRule;
    private CountingIdlingResource mIdlingResource;

    @Before
    public void startMainActivityFromHomeScreen() {
        Log.d(TAG, "startMainActivityFromHomeScreen: ");
        mUserInfoActivityRule = new ActivityTestRule<>(UserInfoActivity.class);
        User user = new User(new Auth("123456", "98908989089", "34545234234"));
        Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), UserInfoActivity.class);
        intent.putExtra(UserInfoActivity.Companion.getUSER(), (Serializable) user);

        UserInfoActivity userInfoActivity = mUserInfoActivityRule.launchActivity(intent);
        mIdlingResource = userInfoActivity.getMIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
        Intents.init();
    }

    @Test
    public void should_show_user_info() {
        onView(withId(R.id.idText)).check(matches(withText("id: 123456")));
        onView(withId(R.id.tokenText)).check(matches(withText("token: 98908989089")));
    }

    @Test
    public void should_show_user_profile() {
        onView(withId(R.id.nameTextView)).check(matches(withText("name: John Smith")));
        onView(withId(R.id.genderTextView)).check(matches(withText("gender: male")));
    }


    @After
    public void tearDown() {
        Intents.release();
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
