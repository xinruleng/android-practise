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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by kevin on 2019/07/18 12:14.
 */
@RunWith(AndroidJUnit4.class)
public class UserInfoActivityTest {
    @Before
    public void startMainActivityFromHomeScreen() {
        Context context = InstrumentationRegistry.getTargetContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

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
    public void should_show_user_info() {
        onView(withId(R.id.idText)).check(matches(withText("id: 123456")));
        onView(withId(R.id.tokenText)).check(matches(withText("token: 98908989089")));
    }

    @Test
    public void should_show_user_profile() {
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.nameTextView)).check(matches(withText("name: John Smith")));
        onView(withId(R.id.genderTextView)).check(matches(withText("gender: male")));
    }
}
