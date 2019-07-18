package com.kevin.newsdemo.user.ui;

import android.content.Context;
import android.content.Intent;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.runner.AndroidJUnit4;
import com.kevin.newsdemo.R;
import org.junit.Before;
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

    @Before
    public void startMainActivityFromHomeScreen() {
        Context context = InstrumentationRegistry.getTargetContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

    }

    @Test
    public void should_start_UserInfoActivity_when_login_succeed() {
        onView(withId(R.id.username)).perform(typeText("valid"));
        //must perform(closeSoftKeyboard()) or will crash with "androidx.test.espresso.PerformException: Error performing 'single click - At Coordinates"
        onView(withId(R.id.password)).perform(typeText("valid")).perform(closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());

        Intents.init();
        // TODO: 2019-07-18 more better way for wait network reqeust
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        intended(hasComponent(UserInfoActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void should_not_start_UserInfoActivity_when_login_failed() {
        onView(withId(R.id.username)).perform(typeText("valid"));
        //must perform(closeSoftKeyboard()) or will crash with "androidx.test.espresso.PerformException: Error performing 'single click - At Coordinates"
        onView(withId(R.id.password)).perform(typeText("valid1")).perform(closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());

        Intents.init();
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        intended(hasComponent(UserInfoActivity.class.getName()), times(0));
        Intents.release();
    }
}
