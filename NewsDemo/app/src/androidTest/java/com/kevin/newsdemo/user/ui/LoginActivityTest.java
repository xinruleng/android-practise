package com.kevin.newsdemo.user.ui;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.kevin.newsdemo.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by kevin on 2019/07/18 11:14.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mRule = new ActivityTestRule<>(LoginActivity.class);
    private CountingIdlingResource mIdlingResource;

    @Before
    public void startMainActivityFromHomeScreen() {
        mIdlingResource = mRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
        Intents.init();
    }

    @Test
    public void should_start_UserInfoActivity_when_login_succeed() {

        onView(withId(R.id.username)).perform(typeText("valid"));
        //must perform(closeSoftKeyboard()) or will crash with "androidx.test.espresso.PerformException: Error performing 'single click - At Coordinates"
        onView(withId(R.id.password)).perform(typeText("valid")).perform(closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());

        intended(hasComponent(UserInfoActivity.class.getName()));
    }

    @Test
    public void should_not_start_UserInfoActivity_when_login_failed() {
        onView(withId(R.id.username)).perform(typeText("valid"));
        //must perform(closeSoftKeyboard()) or will crash with "androidx.test.espresso.PerformException: Error performing 'single click - At Coordinates"
        onView(withId(R.id.password)).perform(typeText("valid1")).perform(closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());

        intended(hasComponent(UserInfoActivity.class.getName()), times(0));
    }

    @After
    public void tearDown() {
        Intents.release();
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
