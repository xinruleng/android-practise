package com.kevin.newsdemo.user.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.kevin.newsdemo.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kevin on 2019/07/18 11:14.
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @get:Rule
    var mRule = ActivityTestRule(LoginActivity::class.java)
    private var mIdlingResource: CountingIdlingResource? = null

    @Before
    fun startMainActivityFromHomeScreen() {

        mIdlingResource = mRule.activity.mIdlingResource
        IdlingRegistry.getInstance().register(mIdlingResource!!)
        Intents.init()

    }

    @Test
    fun should_start_UserInfoActivity_when_login_succeed() {

        onView(withId(R.id.username)).perform(typeText("valid"))
        //must perform(closeSoftKeyboard()) or will crash with "androidx.test.espresso.PerformException: Error performing 'single click - At Coordinates"
        onView(withId(R.id.password)).perform(typeText("valid")).perform(closeSoftKeyboard())
        onView(withId(R.id.login)).perform(click())

        intended(hasComponent(UserInfoActivity::class.java.name))
    }

    @Test
    fun should_not_start_UserInfoActivity_when_login_failed() {
        onView(withId(R.id.username)).perform(typeText("valid"))
        //must perform(closeSoftKeyboard()) or will crash with "androidx.test.espresso.PerformException: Error performing 'single click - At Coordinates"
        onView(withId(R.id.password)).perform(typeText("valid1")).perform(closeSoftKeyboard())
        onView(withId(R.id.login)).perform(click())

        intended(hasComponent(UserInfoActivity::class.java.name), times(0))
    }

    @After
    fun tearDown() {
        Intents.release()
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource!!)
        }
    }

}
